public class Population {
    private Solution[] solutions;  // Tablica rozwiązań reprezentujących populację

    public Population(int size) {
        this.solutions = new Solution[size];  // Inicjalizacja tablicy rozwiązań o podanym rozmiarze
    }

    public int getSize() {
        return solutions.length;  // Zwraca rozmiar populacji (liczbę rozwiązań)
    }

    public Solution getSolution(int index) {
        return solutions[index];  // Zwraca rozwiązanie o podanym indeksie
    }

    public void setSolution(int index, Solution solution) {
        solutions[index] = solution;  // Ustawia rozwiązanie o podanym indeksie na nowe rozwiązanie
    }
}
