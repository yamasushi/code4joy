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
  stroke(128);
  sevenHex(cx   , cy  );
  stroke(255,0,0);
  sevenHex(cx+6 , cy+4);
  stroke(255,255,0);
  sevenHex(cx-3 , cy+5);
  stroke(  0,255,0);
  sevenHex(cx-9 , cy+1);
  stroke(  0,255,255);
  sevenHex(cx-6 , cy-4);
  stroke(255, 0,255);
  sevenHex(cx+3 , cy-5);
  stroke(255,255,255);
  sevenHex(cx+9 , cy-1);
}

void sevenHex(int cx,int cy)
{
  unitHex(cx   , cy  );
  unitHex(cx+3 , cy-1);
  unitHex(cx   , cy-2);
  unitHex(cx-3 , cy-1);
  unitHex(cx-3 , cy+1);
  unitHex(cx   , cy+2);
  unitHex(cx+3 , cy+1);
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
