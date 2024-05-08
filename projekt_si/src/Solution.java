public class Solution {
    private int[] assignment;  // Tablica przypisania modułów do procesorów
    private int bottleneckProcessorIndex;  // Indeks procesora o największym obciążeniu
    private int bottleneckProcessorTime;  // Czas działania procesora o największym obciążeniu

    public Solution(int numModules) {
        this.assignment = new int[numModules];  // Inicjalizacja tablicy przypisań
        this.bottleneckProcessorIndex = -1;  // Inicjalizacja indeksu procesora o największym obciążeniu
        this.bottleneckProcessorTime = 0;  // Inicjalizacja czasu działania procesora o największym obciążeniu
    }

    public int[] getAssignment() {
        return assignment;  // Zwraca tablicę przypisań
    }

    public int getAssignment(int index) {
        return assignment[index];  // Zwraca przypisanie dla konkretnego modułu o podanym indeksie
    }

    public void setAssignment(int index, int processorIndex) {
        assignment[index] = processorIndex;  // Ustawia przypisanie dla konkretnego modułu o podanym indeksie
    }

    public int getBottleneckProcessorIndex() {
        return bottleneckProcessorIndex;  // Zwraca indeks procesora o największym obciążeniu
    }

    public void setBottleneckProcessorIndex(int processorIndex) {
        bottleneckProcessorIndex = processorIndex;  // Ustawia indeks procesora o największym obciążeniu
    }

    public int getBottleneckProcessorTime() {
        return bottleneckProcessorTime;  // Zwraca czas działania procesora o największym obciążeniu
    }

    public void setBottleneckProcessorTime(int processorTime) {
        bottleneckProcessorTime = processorTime;  // Ustawia czas działania procesora o największym obciążeniu
    }
}
