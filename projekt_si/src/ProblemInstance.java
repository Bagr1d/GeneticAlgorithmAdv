public class ProblemInstance {
    private int numModules;  // Liczba modułów w instancji problemu
    private int numProcessors;  // Liczba procesorów w instancji problemu
    private int[] moduleTimes;  // Tablica czasów wykonania poszczególnych modułów
    private int[][] transmissionTimes;  // Tablica czasów transmisji pomiędzy modułami

    public ProblemInstance(int numModules, int numProcessors, int[] moduleTimes, int[][] transmissionTimes) {
        this.numModules = numModules;  // Inicjalizacja liczby modułów
        this.numProcessors = numProcessors;  // Inicjalizacja liczby procesorów
        this.moduleTimes = moduleTimes;  // Inicjalizacja tablicy czasów wykonania modułów
        this.transmissionTimes = transmissionTimes;  // Inicjalizacja tablicy czasów transmisji
    }

    public int getNumModules() {
        return numModules;  // Zwraca liczbę modułów
    }

    public int getNumProcessors() {
        return numProcessors;  // Zwraca liczbę procesorów
    }

    public int getModuleTime(int index) {
        return moduleTimes[index];  // Zwraca czas wykonania modułu o podanym indeksie
    }

    public int getTransmissionTime(int module1Index, int module2Index) {
        return transmissionTimes[module1Index][module2Index];  // Zwraca czas transmisji pomiędzy modułami o podanych indeksach
    }
}
