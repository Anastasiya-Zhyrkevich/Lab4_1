#pragma version(1)
#pragma rs java_package_name(com.example.nosti.toolbar)

uint8_t brightness;

uchar4 __attribute__((kernel)) invert(uchar4 in, uint32_t x, uint32_t y) {
  uchar4 out = in;
  //out.r += brightness;
  //out.g += brightness;
  //out.b += brightness;
  if (out.r + brightness> 255){
      out.r = 255;
  }
  else
    out.r += brightness;
  if (out.b + brightness > 255){
      out.b = 255;
  }
  else
    out.b += brightness;
  if (out.g + brightness > 255){
      out.g = 255;
  }
  else
    out.g += brightness;
  return out;
}
