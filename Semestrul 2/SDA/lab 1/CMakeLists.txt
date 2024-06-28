cmake_minimum_required(VERSION 3.27)
project(lab_1)

set(CMAKE_CXX_STANDARD 14)

include_directories(.)

add_executable(lab_1
        App.cpp
        IteratorMultime.cpp
        IteratorMultime.h
        Multime.cpp
        Multime.h
        TestExtins.cpp
        TestExtins.h
        TestScurt.cpp
        TestScurt.h)
