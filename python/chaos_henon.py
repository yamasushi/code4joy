# henon attractor
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

# from : http://codezine.jp/article/detail/327?p=2

h_sq =lambda it:it*it
h_cu =lambda it:it*it*it
h_si =lambda it:0.8*math.sin(it)
h_in =lambda it:0.1 / it

henon = h_sq
alpha = 0.47 * math.pi
# 0.07
# 0.30
# 0.39
# 0.47
# 0.61
# 0.84
sin_alpha = math.sin(alpha)
cos_alpha = math.cos(alpha)

num_trajectory           = 100
num_point_per_trajectory = 1000

ratio     = 300.0
imgWidth  = 500
imgHeight = 500
offsetx   = imgWidth /2
offsety   = imgHeight/2
imgType   = "png"
fileName  = "a.png"

oval_r    = 1.0
num_ring  = 15

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
		for i in range(num_point_per_trajectory) :
			if limitNegative > x or limitPositive < x  or limitNegative > y or limitPositive < y :
				break
			
			ix = int(               ratio*x + offsetx   )
			iy = int( imgHeight - ( ratio*y + offsety ) )
			if imgWidth > ix >= 0 and imgHeight > iy >= 0 :
				g.drawLine(ix,iy,ix,iy)
			
			temp  = y - henon(x)
			(x,y) = (x*cos_alpha - temp*sin_alpha,  
					 x*sin_alpha + temp*cos_alpha) 

createPictureFile(imgWidth , imgHeight , imgType , fileName , drawProc )
