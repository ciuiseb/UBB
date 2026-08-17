#!/bin/bash

if [ ! -d "files" ]; then mkdir files; fi
javac *.java
if [ $? -ne 0 ]; then
    exit 1
fi

java Generator

calculate_average() {
    local label="$1"
    shift
    local cmd="$@"
    local total_time=0
    local runs=10

    echo "Testing: $label"

    for ((i=1; i<=runs; i++)); do
        output=$($cmd)
        time_ms=$(echo "$output" | grep "Time:" | awk '{print $2}')

        if [[ -z "$time_ms" ]]; then
            echo "  Run $i: Failed to capture time."
            continue
        fi

        total_time=$((total_time + time_ms))
    done

    local avg=$((total_time / runs))
    echo "  Average over 10 runs: $avg ms"
    echo ""
}

calculate_average "Sequential" "java Secvential"

echo "--- Parallel Tests (1 Reader) ---"
for P in 4 8 16; do
    calculate_average "Parallel [Processes=$P, Readers=1]" "java Paralel $P 1"
done

echo "--- Parallel Tests (2 Readers) ---"
for P in 4 8 16; do
    calculate_average "Parallel [Processes=$P, Readers=2]" "java Paralel $P 2"
done

echo "--- Benchmark Complete ---"