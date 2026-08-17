cmake_minimum_required(VERSION 3.30)
project(testmpi)

set(CMAKE_CXX_STANDARD 20)
find_package(MPI REQUIRED)
add_executable(testmpi main.cpp)
target_link_libraries(testmpi PRIVATE MPI::MPI_CXX)

