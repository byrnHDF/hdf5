cmake_minimum_required (VERSION 3.18)
project (HDF5_JAVA_SRC C)

set_directory_properties(PROPERTIES INCLUDE_DIRECTORIES "${HDF5_JAVA_INCLUDE_DIRECTORIES}")

#-----------------------------------------------------------------------------
# Traverse source subdirectory
#-----------------------------------------------------------------------------
add_subdirectory (jni)
add_subdirectory (hdf)
