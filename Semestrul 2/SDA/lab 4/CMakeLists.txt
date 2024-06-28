cmake_minimum_required(VERSION 3.27)
project(lab_4)

set(CMAKE_CXX_STANDARD 14)

include_directories(.)

add_executable(lab_4
        App.cpp
        IteratorMDO.cpp
        IteratorMDO.h
        MDO.cpp
        MDO.h
        TestExtins.cpp
        TestExtins.h
        TestScurt.cpp
        TestScurt.h)
