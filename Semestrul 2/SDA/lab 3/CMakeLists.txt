cmake_minimum_required(VERSION 3.27)
project(lab_3)

set(CMAKE_CXX_STANDARD 23)

include_directories(.)

add_executable(lab_3
        App.cpp
        IteratorMD.cpp
        IteratorMD.h
        MD.cpp
        MD.h
        Node.h
		TestExtins.cpp
	TestExtins.h
	TestScurt.cpp
	TestScurt.h)
