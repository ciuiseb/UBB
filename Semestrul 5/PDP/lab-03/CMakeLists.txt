cmake_minimum_required(VERSION 3.30)
project(sct)

set(CMAKE_CXX_STANDARD 20)
find_package(MPI REQUIRED)
add_executable(sct scatter_gather.cpp)
target_link_libraries(sct PRIVATE MPI::MPI_CXX)
