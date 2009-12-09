float sqrt3 = sqrt(3);
float ratio = 20;
void setup()
{
  size(700,700);
  background(0);
}

void draw()
{
  int cx = 15;
  int cy = 10;
  //
  int sx  = 0;
  int sy  = 0;
  int ex  = 0;
  int ey  = 0;
  //
  stroke(128);
  sevenHex(cx   , cy  );
  //
  ex = cx + 6;
  ey = cy + 4;
  //
  stroke(255,0,0);
  sx = ex;
  sy = ey;
  ex = sx - 9;
  ey = sy + 1;
  sevenHex   (sx , sy);
  lineLattice(sx , sy , ex , ey);
  //
  stroke(255,255,0);
  sx = ex;
  sy = ey;
  ex = sx - 6;
  ey = sy - 4;
  sevenHex   (sx , sy);
  lineLattice(sx , sy , ex , ey);
  //
  stroke(  0,255,0);
  sx = ex;
  sy = ey;
  ex = sx + 3;
  ey = sy - 5;
  sevenHex   (sx , sy);
  lineLattice(sx , sy , ex , ey);
  //
  stroke(  0,255,255);
  sx = ex;
  sy = ey;
  ex = sx + 9;
  ey = sy - 1;
  sevenHex   (sx , sy);
  lineLattice(sx , sy , ex , ey);
  //
  stroke(255, 0,255);
  sx = ex;
  sy = ey;
  ex = sx + 6;
  ey = sy + 4;
  sevenHex   (sx , sy);
  lineLattice(sx , sy , ex , ey);
  //
  stroke(255,255,255);
  sx = ex;
  sy = ey;
  ex = sx - 3;
  ey = sy + 5;
  sevenHex   (sx , sy);
  lineLattice(sx , sy , ex , ey);
  
}

void sevenHex(int cx,int cy)
{
  //
  int sx  = 0;
  int sy  = 0;
  int ex  = 0;
  int ey  = 0;
  //
  unitHex(cx   , cy  );
  //
  ex = cx;
  ey = cy - 2;
  //
  sx = ex;
  sy = ey;
  ex = sx - 3;
  ey = sy + 1;
  unitHex    (sx , sy);
  lineLattice(sx , sy , ex , ey);
  //
  sx = ex;
  sy = ey;
  ex = sx + 0;
  ey = sy + 2;
  unitHex    (sx , sy);
  lineLattice(sx , sy , ex , ey);
  //
  sx = ex;
  sy = ey;
  ex = sx + 3;
  ey = sy + 1;
  unitHex    (sx , sy);
  lineLattice(sx , sy , ex , ey);
  //
  sx = ex;
  sy = ey;
  ex = sx + 3;
  ey = sy - 1;
  unitHex    (sx , sy);
  lineLattice(sx , sy , ex , ey);
  //
  sx = ex;
  sy = ey;
  ex = sx + 0;
  ey = sy - 2;
  unitHex    (sx , sy);
  lineLattice(sx , sy , ex , ey);
  //
  sx = ex;
  sy = ey;
  ex = sx - 3;
  ey = sy - 1;
  unitHex    (sx , sy);
  lineLattice(sx , sy , ex , ey);
  //
}

void unitHex(int cx,int cy)
{
  lineLattice(cx+2,cy  ,cx+1,cy-1);
  lineLattice(cx+1,cy-1,cx-1,cy-1);
  lineLattice(cx-1,cy-1,cx-2,cy  );
  lineLattice(cx-2,cy  ,cx-1,cy+1);
  lineLattice(cx-1,cy+1,cx+1,cy+1);
  lineLattice(cx+1,cy+1,cx+2,cy  );
}
void lineLattice(int sx,int sy,int ex ,int ey)
{
  line( sx*ratio , sy*ratio*sqrt3 , ex*ratio , ey*ratio*sqrt3 );
}
