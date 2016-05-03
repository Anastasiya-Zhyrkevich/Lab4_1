#pragma version(1)
#pragma rs java_package_name(com.example.nosti.toolbar)

uint8_t brightness;
rs_matrix4x4 colorMat;

uchar4 __attribute__((kernel)) extract(uchar4 in, uint32_t x, uint32_t y) {
    uchar4 out;
    float4 pixel = convert_float4(in);
    pixel = rsMatrixMultiply(&colorMat, pixel);
    out = convert_uchar4(pixel);
    return out;
  }
