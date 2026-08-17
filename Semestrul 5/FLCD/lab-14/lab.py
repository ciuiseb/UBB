import subprocess

program_filename = "program.txt"
asm_filename = "program.asm"
obj_name = "program.obj"
exe_name = "program.exe"
input_value = "10\n10\n"

with open(asm_filename, "w") as f:
    subprocess.run(["compiler.exe", program_filename], stdout=f)

subprocess.run(["nasm", "-fobj", asm_filename, "-o", obj_name], check=True)
subprocess.run(["alink", "-oPE", "-subsys", "console", "-entry", "start", obj_name, "-o", exe_name], check=True)

try:
    result = subprocess.run(
        [exe_name],
        input=input_value,
        text=True,
        capture_output=True,
        timeout=3
    )
    print("OUTPUT PROGRAM:")
    print(result.stdout)
except subprocess.TimeoutExpired:
    print("Eroare")