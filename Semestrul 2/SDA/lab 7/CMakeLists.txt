cmake_minimum_required(VERSION 3.27)
project(lab_7)

set(CMAKE_CXX_STANDARD 14)

include_directories(.)

add_executable(lab_7
        AB.cpp
        AB.h
        App.cpp
        IteratorAB.cpp
        IteratorAB.h
        Node.h
        TestExtins.cpp
        TestExtins.h
        TestScurt.cpp
        TestScurt.h)
