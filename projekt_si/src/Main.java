import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    private static final int POPULATION_SIZE = 50;
    private static final int MAX_ITERATIONS = 1;
    private static final double CROSSOVER_RATE = 0.8;
    private static final double MUTATION_RATE = 0.1;

    public static void main(String[] args) {
        // Inicjalizacja problemu i populacji startowej
        for(int i=0; i <= 30; i++) {
            // Odczytanie parametrów z pliku wejściowego
            File inputFile = new File("C:\\Users\\patwi\\Desktop\\BaraBuch\\IT\\4sem\\si\\plik.txt");
            // Przetwarzanie danych wejściowych i rozwiązanie problemu
            ProblemInstance problem = readInput(String.valueOf(inputFile));
            Solution solution = solve(problem);
            // Zapis rozwiązania do pliku wyjściowego
            saveSolutionToFile("output.txt", solution);
        }
    }

    private static ProblemInstance readInput(String filePath) {
        try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            // Odczytanie liczby modułów i procesorów
            String[] firstLine = bufferedReader.readLine().split(" ");
            int numModules = Integer.parseInt(firstLine[0]);
            int numProcessors = Integer.parseInt(firstLine[1]);

            // Odczytanie czasów modułów
            int[] moduleTimes = new int[numModules];
            String[] moduleTimesLine = bufferedReader.readLine().split(" ");
            for (int i = 0; i < numModules; i++) {
                moduleTimes[i] = Integer.parseInt(moduleTimesLine[i]);
            }

            // Odczytanie czasów transmisji
            int[][] transmissionTimes = new int[numModules][numModules];
            for (int i = 0; i < numModules; i++) {
                String[] transmissionTimesLine = bufferedReader.readLine().split(" ");
                for (int j = 0; j < numModules; j++) {
                    transmissionTimes[i][j] = Integer.parseInt(transmissionTimesLine[j]);
                }
            }

            bufferedReader.close();

            return new ProblemInstance(numModules, numProcessors, moduleTimes, transmissionTimes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    private static Solution solve(ProblemInstance problem) {
        Population population = initializePopulation(problem);
        evaluatePopulation(population, problem);
        for (int i = 0; i < MAX_ITERATIONS; i++) {
            Population offspring = generateOffspring(population, problem);
            evaluatePopulation(offspring, problem);
            population = selectNextGeneration(population, offspring);
        }
        return getBestSolution(population);
    }

    private static Population initializePopulation(ProblemInstance problem) {
        Population population = new Population(POPULATION_SIZE);  // Inicjalizacja populacji startowej
        for (int i = 0; i < POPULATION_SIZE; i++) {
            Solution randomSolution = generateRandomSolution(problem);
            population.setSolution(i, randomSolution); // Tworzenie losowych rozwiązań
        }
        return population; // Zwróć populację startową
    }

    private static Solution generateRandomSolution(ProblemInstance problem) {
        Solution solution = new Solution(problem.getNumModules());
        for (int i = 0; i < problem.getNumModules(); i++) {
            int randomProcessorIndex = getRandomProcessorIndex(problem.getNumProcessors());
            solution.setAssignment(i, randomProcessorIndex);
        }
        return solution;
    }


    private static int getRandomProcessorIndex(int numProcessors) {
        Random random = new Random();
        return random.nextInt(numProcessors) + 1;  // Numeracja procesorów od 1 do numProcessors
    }

    private static void evaluatePopulation(Population population, ProblemInstance problem) {
        for (int i = 0; i < population.getSize(); i++) {
            Solution solution = population.getSolution(i);
            int bottleneckProcessorIndex = calculateBottleneckProcessor(solution, problem);
            int bottleneckProcessorTime = calculateBottleneckProcessorTime(solution, problem, bottleneckProcessorIndex);
            solution.setBottleneckProcessorIndex(bottleneckProcessorIndex);
            solution.setBottleneckProcessorTime(bottleneckProcessorTime);
        }
    }

    private static int calculateBottleneckProcessor(Solution solution, ProblemInstance problem) {
        int[] processorTimes = new int[problem.getNumProcessors()];
        Arrays.fill(processorTimes, 0);
        int[] assignments = solution.getAssignment();
        for (int i = 0; i < assignments.length; i++) {
            int moduleTime = problem.getModuleTime(i);
            int moduleProcessor = assignments[i];
            processorTimes[moduleProcessor - 1] += moduleTime;
        }
        int bottleneckProcessor = 0;
        int maxTime = 0;
        for (int i = 0; i < processorTimes.length; i++) {
            if (processorTimes[i] > maxTime) {
                bottleneckProcessor = i + 1;  // Numeracja procesorów od 1 do numProcessors
                maxTime = processorTimes[i];
            }
        }
        return bottleneckProcessor;
    }


    private static int calculateBottleneckProcessorTime(Solution solution, ProblemInstance problem, int bottleneckProcessorIndex) {
        int bottleneckProcessorTime = 0;
        int[] processorTimes = new int[problem.getNumProcessors()];
        Arrays.fill(processorTimes, 0);
        int[] assignments = solution.getAssignment();
        for (int i = 0; i < assignments.length; i++) {
            int moduleTime = problem.getModuleTime(i);
            int moduleProcessor = assignments[i];
            processorTimes[moduleProcessor - 1] += moduleTime;
            if (moduleProcessor == bottleneckProcessorIndex) {
                bottleneckProcessorTime += moduleTime;
            }
        }
        int lastModuleIndex = findLastModuleIndex(solution, bottleneckProcessorIndex);
        int firstModuleIndex = findFirstModuleIndex(solution, bottleneckProcessorIndex);
        if (lastModuleIndex != -1 && firstModuleIndex != -1) {
            int transmissionTime = problem.getTransmissionTime(lastModuleIndex, firstModuleIndex);
            bottleneckProcessorTime += transmissionTime;
        }
        return bottleneckProcessorTime;
        // Oblicz czas procesora dla bottlenecka dla danego rozwiązania
        // Zwróć czas procesora dla bottlenecka
    }


    private static int findLastModuleIndex(Solution solution, int processorIndex) {
        int[] assignments = solution.getAssignment();
        for (int i = assignments.length - 1; i >= 0; i--) {
            if (assignments[i] == processorIndex) {
                return i;
            }
        }
        return -1;
    }


    private static int findFirstModuleIndex(Solution solution, int processorIndex) {
        int[] assignments = solution.getAssignment();
        for (int i = 0; i < assignments.length; i++) {
            if (assignments[i] == processorIndex) {
                return i;
            }
        }
        return -1;
    }


    private static Population generateOffspring(Population population, ProblemInstance problem) {
        Population offspring = new Population(population.getSize());
        for (int i = 0; i < population.getSize(); i++) {
            Solution parent1 = selectParent(population);
            Solution parent2 = selectParent(population);
            Solution child = crossover(parent1, parent2);
            mutate(child, problem);
            offspring.setSolution(i, child);
        }
        return offspring;
    }

    private static Solution selectParent(Population population) {
        Random random = new Random();
        int parentIndex = random.nextInt(population.getSize());
        return population.getSolution(parentIndex);
    }

    private static Solution crossover(Solution parent1, Solution parent2) {
        int[] parent1Assignments = parent1.getAssignment();
        int[] parent2Assignments = parent2.getAssignment();
        int length = parent1Assignments.length;
        Solution child = new Solution(length);
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            if (random.nextDouble() <= CROSSOVER_RATE) {
                child.setAssignment(i, parent1Assignments[i]);
            } else {
                child.setAssignment(i, parent2Assignments[i]);
            }
        }

        return child;
    }


    private static void mutate(Solution solution, ProblemInstance problem) {
        Random random = new Random();
        for (int i = 0; i < solution.getAssignment().length; i++) {
            if (random.nextDouble() <= MUTATION_RATE) {
                solution.setAssignment(i, getRandomProcessorIndex(problem.getNumProcessors()));
            }
        }
    }

    private static Population selectNextGeneration(Population currentGeneration, Population offspring) {
        Population nextGeneration = new Population(currentGeneration.getSize());
        for (int i = 0; i < currentGeneration.getSize(); i++) {
            Solution currentSolution = currentGeneration.getSolution(i);
            Solution offspringSolution = offspring.getSolution(i);
            if (offspringSolution.getBottleneckProcessorTime() <= currentSolution.getBottleneckProcessorTime()) {
                nextGeneration.setSolution(i, offspringSolution);
            } else {
                nextGeneration.setSolution(i, currentSolution);
            }
        }
        return nextGeneration;
    }

    private static Solution getBestSolution(Population population) {
        Solution bestSolution = population.getSolution(0);
        for (int i = 1; i < population.getSize(); i++) {
            Solution currentSolution = population.getSolution(i);
            if (currentSolution.getBottleneckProcessorTime() < bestSolution.getBottleneckProcessorTime()) {
                bestSolution = currentSolution;
            }
        }
        return bestSolution;
    }

    private static void saveSolutionToFile(String filePath, Solution solution) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));

            FileWriter writer = new FileWriter(filePath);
            StringBuilder sb = new StringBuilder();

            // Zbierz zadania przypisane do każdego procesora.
            Map<Integer, List<Integer>> processorTasks = new HashMap<>();
            for (int i = 0; i < solution.getAssignment().length; i++) {
                int processor = solution.getAssignment(i);
                if (!processorTasks.containsKey(processor)) {
                    processorTasks.put(processor, new ArrayList<>());
                }
                processorTasks.get(processor).add(i + 1);
            }

            //Buduj ciąg wyjściowy.
            for (Map.Entry<Integer, List<Integer>> entry : processorTasks.entrySet()) {
                sb.append("p").append(entry.getKey()).append("; ");
                List<Integer> tasks = entry.getValue();
                for (int i = 0; i < tasks.size(); i++) {
                    sb.append("").append(tasks.get(i));
                    if (i < tasks.size() - 1) {
                        sb.append("; ");
                    }
                }
                sb.append("; ");
            }

            //Dodaj informacje o procesorze o wąskim gardle.
            sb.append("p-bot=; ").append(solution.getBottleneckProcessorIndex()).append("; ");
            sb.append("Cb=; ").append(solution.getBottleneckProcessorTime()).append(";");

            writer.write(content + "\r\n" + sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
