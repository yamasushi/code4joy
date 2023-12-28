# Gumowski-Mira attractor
# output to a.png

import java
import math
from java.awt       import *
from java.awt.image import *
from javax.imageio  import *
from java.io        import *
from java.util      import *

def createPictureFile(imgWidth , imgHeight , imgType , fileName , op):
	bi = BufferedImage(imgWidth,imgHeight,BufferedImage.TYPE_BYTE_GRAY)
	g  = bi.createGraphics()
	g.setPaint(Color.WHITE)
	g.fillRect(0,0,imgWidth,imgHeight)
	g.setColor(Color.BLACK)
	op(g)
	outFile = File(fileName)
	ImageIO.write(bi,imgType,outFile)


xa   =  0.0005
xb   =  1.0
a    = -0.48
b    =  0.9924

def f_0(x) : 
	x_x = x*x
	return a * x + ( 2*(1-a)*x_x / (1+x_x) )

def f_1(x) : 
	x_x = x*x
	return a * x + ( 2*(1-a)*x_x / ((1+x_x)*(1+x_x)) )

# from http://codezine.jp/article/detail/327?p=2
def f_2(x) :
	x_x = x*x
	return a * x + ( 2*(1-a)*x / (1+x_x) )

# from http://codezine.jp/article/detail/327?p=2
def f_3(x) :
	x_x = x*x
	return a * x + ( 2*(1-a) / (1+x_x) )

def g_0(y) :
	return b *y + xa * (1- xb*y*y)*y

gm_g = g_0
gm_f = f_0

num_trajectory           = 100
num_point_per_trajectory = 1000

ratio     = 20.0
imgWidth  = 500
imgHeight = 500
offsetx   = imgWidth /2
offsety   = imgHeight/2
imgType   = "png"
fileName  = "a.png"

oval_r    = 0.5
num_ring  = 10

limitPositive = 10e6
limitNegative =-limitPositive

def drawProc(g) :
	rand = java.util.Random()
	for j in range(num_trajectory) :
		iring = (j % num_ring) + 1 
		r     = (oval_r * iring)/num_ring
		theta = rand.nextDouble() * math.pi * 2
		#
		(x,y) = ( r*math.cos( theta ) , r*math.sin( theta ) )
		#
		u = gm_f(x)
		for i in range(num_point_per_trajectory) :
			if limitNegative > x or limitPositive < x  or limitNegative > y or limitPositive < y :
				break
			
			ix = int(               ratio*x + offsetx   )
			iy = int( imgHeight - ( ratio*y + offsety ) )
			if imgWidth > ix >= 0 and imgHeight > iy >= 0 :
				g.drawLine(ix,iy,ix,iy)
			
			xx = gm_g(y) + u
			u  = gm_f(xx)
			yy = u - x
			
			(x,y) = (xx,yy)

createPictureFile(imgWidth , imgHeight , imgType , fileName , drawProc )
