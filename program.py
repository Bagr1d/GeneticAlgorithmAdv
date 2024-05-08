import random
import os

def generate_input_file(num_modules, num_processors):
    current_dir = os.path.dirname(os.path.abspath(__file__))
    file_path = os.path.join(current_dir, f"input_m{num_modules}_p{num_processors}.txt")
    with open(file_path, 'w') as file:
        # Write the number of modules and processors
        file.write(f"{num_modules} {num_processors}\n")

        # Generate random module times
        module_times = [random.randint(1, 100) for _ in range(num_modules)]
        file.write(' '.join(map(str, module_times)) + '\n')

        # Generate random transmission times
        transmission_times = [[random.randint(0, 500) for _ in range(num_modules)] for _ in range(num_modules)]
        for row in transmission_times:
            file.write(' '.join(map(str, row)) + '\n')

    print(f"Generated input file: {file_path}")

# Example usage
num_modules = int(input("Enter the number of modules: "))
num_processors = int(input("Enter the number of processors: "))
generate_input_file(num_modules, num_processors)