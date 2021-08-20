
cmake_minimum_required(VERSION 3.10.2)
project("ndkdemo")

add_library(
        native-lib
        SHARED
        native-lib.cpp
        bitmap_util.cpp)

find_library( # Sets the name of the path variable.
        log-lib
        log)

target_link_libraries( # Specifies the target library.
        native-lib
        jnigraphics
        ${log-lib})