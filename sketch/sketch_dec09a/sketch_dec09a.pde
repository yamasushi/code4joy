float sqrt3 = sqrt(3);
float ratio = 3;

// matrix((a,b),(c,d))
int a = -2;
int b =  3;
int c =  1;
int d =  2;
//

int x_ab(int x,int y)
{
  return a*x + b*y;
}

int y_cd(int x,int y)
{
  return c*x + d*y;
}

void setup()
{
  size(700,700);
  background(0);
  stroke(255);
  smooth();
  noLoop();
  //frameRate(2);
}

void draw()
{
  int cx =130;
  int cy = 70;
  //
  hexHexPath(3,cx,cy);
}


void hexHexPath(int degree,int cx,int cy)
{
  if(degree < 0){
    stroke(255);
    unitHex(cx,cy);
    //delay(1);
    return;
  }
  //println(degree);
  int ux=0;
  int uy=0;
  int vx=0;
  int vy=0;
  int wx=0;
  int wy=0;
  int _ux=0;
  int _uy=0;
  int _vx=0;
  int _vy=0;
  int _wx=0;
  int _wy=0;
  //
  ux =  3;
  uy = -1;
  vx =  0;
  vy = -2;
  wx = -3;
  wy = -1;
  _ux = ux;
  _uy = uy;
  _vx = vx;
  _vy = vy;
  _wx = wx;
  _wy = wy;
  if( degree > 0 ){
    for(int i=1;i<=degree;i++){
      ux =  x_ab(_ux , _uy);
      uy =  y_cd(_ux , _uy);
      vx =  x_ab(_vx , _vy);
      vy =  y_cd(_vx , _vy);
      wx =  x_ab(_wx , _wy);
      wy =  y_cd(_wx , _wy);
      _ux = ux;
      _uy = uy;
      _vx = vx;
      _vy = vy;
      _wx = wx;
      _wy = wy;
      //
    }
  }
  //println(degree);
  //
  int sx = cx;
  int sy = cy;
  int ex = sx - vx;
  int ey = sy - vy;
  //
  int alpha = 80;
  //
  stroke(255,0,0,alpha);
  lineLattice(sx , sy , ex , ey);
  //
  hexHexPath(degree-1 , sx , sy);
  //
  sx = ex;
  sy = ey;
  ex = sx + ux;
  ey = sy + uy;
  stroke(0,255,0,alpha);
  lineLattice(sx , sy , ex , ey);
  hexHexPath(degree-1 , ex , ey);
  //
  sx = ex;
  sy = ey;
  ex = sx + vx;
  ey = sy + vy;
  stroke(0,255,0,alpha);
  lineLattice(sx , sy , ex , ey);
  hexHexPath(degree-1 , ex , ey);
  //
  sx = ex;
  sy = ey;
  ex = sx + wx;
  ey = sy + wy;
  stroke(0,255,0,alpha);
  lineLattice(sx , sy , ex , ey);
  hexHexPath(degree-1 , ex , ey);
  //
  sx = ex;
  sy = ey;
  ex = sx - ux;
  ey = sy - uy;
  stroke(0,255,0,alpha);
  lineLattice(sx , sy , ex , ey);
  hexHexPath(degree-1 , ex , ey);
  //
  sx = ex;
  sy = ey;
  ex = sx - vx;
  ey = sy - vy;
  stroke(0,255,0,alpha);
  lineLattice(sx , sy , ex , ey);
  hexHexPath(degree-1 , ex , ey);
  //
  sx = ex;
  sy = ey;
  ex = sx - wx;
  ey = sy - wy;
  stroke(0,255,0,alpha);
  lineLattice(sx , sy , ex , ey);
  hexHexPath(degree-1 , ex , ey);
  //
}

void hexPath(int cx,int cy,int ux,int uy,int vx,int vy,int wx,int wy)
{
  int sx  = 0;
  int sy  = 0;
  int ex  = 0;
  int ey  = 0;
  //
  ex = cx -vx;
  ey = cy -vy;
  lineLattice(cx , cy , ex , ey);
  //
  sx = ex;
  sy = ey;
  ex = sx + ux;
  ey = sy + uy;
  lineLattice(sx , sy , ex , ey);
  //
  sx = ex;
  sy = ey;
  ex = sx + vx;
  ey = sy + vy;
  lineLattice(sx , sy , ex , ey);
  //
  sx = ex;
  sy = ey;
  ex = sx + wx;
  ey = sy + wy;
  lineLattice(sx , sy , ex , ey);
  //
  sx = ex;
  sy = ey;
  ex = sx - ux;
  ey = sy - uy;
  lineLattice(sx , sy , ex , ey);
  //
  sx = ex;
  sy = ey;
  ex = sx - vx;
  ey = sy - vy;
  lineLattice(sx , sy , ex , ey);
  //
  sx = ex;
  sy = ey;
  ex = sx - wx;
  ey = sy - wy;
  lineLattice(sx , sy , ex , ey);
}


void sevenSevenHex(int cx , int cy)
{
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
