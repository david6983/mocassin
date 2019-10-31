cmake_minimum_required(VERSION 3.0.0)
project${project_name})

## [BEGIN_BUILD_${project_name}_Slist]

# add all sources in ${project_name}/
file(GLOB SOURCES "${project_name}/*.c")

# create static library "lib${project_name}lib" from SOURCES
<#assign src = "$\{SOURCES}">
add_library(${project_name}lib STATIC ${src})

# install the library to usr/local/
install(TARGETS ${project_name}lib DESTINATION lib)

## [END_BUILD_LIB${project_name}LIB]
