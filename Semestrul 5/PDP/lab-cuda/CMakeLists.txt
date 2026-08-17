cmake_minimum_required(VERSION 3.25)
project(lab_cuda CUDA)

set(CMAKE_CUDA_STANDARD 17)
set(CMAKE_CUDA_ARCHITECTURES native)

add_executable(gen_data PopulateFile.cu)
add_executable(cuda_threads CudaThreads.cu
#        OutputValidator.cu
)

set_target_properties(gen_data PROPERTIES CUDA_SEPARABLE_COMPILATION ON)
set_target_properties(cuda_threads PROPERTIES CUDA_SEPARABLE_COMPILATION ON)