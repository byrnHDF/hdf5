HDF5 "tints4dims.h5" {
DATASET "FourDimInts" {
   DATATYPE  H5T_STD_U32LE
   DATASPACE  SIMPLE { ( 4, 6, 8, 10 ) / ( 4, 6, 8, 10 ) }
   SUBSET {
      START ( 0, 0, 0, 0 );
      STRIDE ( 2, 2, 1, 1 );
      COUNT ( 2, 2, 4, 4 );
      BLOCK ( 1, 2, 1, 1 );
      DATA {
      (0,0,0,0): 0, 1, 2, 3, 80, 81, 82, 83,
      (0,0,1,0): 10, 11, 12, 13, 90, 91, 92, 93,
      (0,0,2,0): 20, 21, 22, 23, 100, 101, 102, 103,
      (0,0,3,0): 30, 31, 32, 33, 110, 111, 112, 113
      (0,2,0,0): 160, 161, 162, 163, 240, 241, 242, 243,
      (0,2,1,0): 170, 171, 172, 173, 250, 251, 252, 253,
      (0,2,2,0): 180, 181, 182, 183, 260, 261, 262, 263,
      (0,2,3,0): 190, 191, 192, 193, 270, 271, 272, 273
      (2,0,0,0): 960, 961, 962, 963, 1040, 1041, 1042, 1043,
      (2,0,1,0): 970, 971, 972, 973, 1050, 1051, 1052, 1053,
      (2,0,2,0): 980, 981, 982, 983, 1060, 1061, 1062, 1063,
      (2,0,3,0): 990, 991, 992, 993, 1070, 1071, 1072, 1073
      (2,2,0,0): 1120, 1121, 1122, 1123, 1200, 1201, 1202, 1203,
      (2,2,1,0): 1130, 1131, 1132, 1133, 1210, 1211, 1212, 1213,
      (2,2,2,0): 1140, 1141, 1142, 1143, 1220, 1221, 1222, 1223,
      (2,2,3,0): 1150, 1151, 1152, 1153, 1230, 1231, 1232, 1233
      }
   }
}
}