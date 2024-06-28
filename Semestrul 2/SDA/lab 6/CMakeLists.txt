cmake_minimum_required(VERSION 3.27)
project(lab_6)

set(CMAKE_CXX_STANDARD 14)

include_directories(.)

add_executable(lab_6
        App.cpp
        Matrice.cpp
        Matrice.h
        TestExtins.cpp
        TestExtins.h
        TestScurt.cpp
        TestScurt.h)
