# ikeda attractor
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

a = 1.0
b = 0.9
k = 0.4
p = 6.0

num_trajectory           = 100
num_point_per_trajectory = 1000

ratio     = 100.0
imgWidth  = 500
imgHeight = 500
offsetx   = imgWidth /2
offsety   = imgHeight/2
imgType   = "png"
fileName  = "a.png"

limitPositive = 10e6
limitNegative =-limitPositive

def drawProc(g) :
	rand = java.util.Random()
	for j in range(num_trajectory) :
		theta= rand.nextDouble() * math.pi * 2.0
		(x,y) = ( math.cos( theta ) , math.sin( theta ) )
		#
		for i in range(num_point_per_trajectory) :
			if limitNegative > x or limitPositive < x  or limitNegative > y or limitPositive < y :
				break
			
			ix = int(              ratio*x + offsetx    )
			iy = int( imgHeight - ( ratio*y + offsety ) )
			g.drawLine(ix,iy,ix,iy)
			
			tn = k - p/(1.0 + (x*x + y*y) )
			
			sinTn = math.sin(tn)
			cosTn = math.cos(tn)
			
			(x,y) =( b * ( x*cosTn - y*sinTn ) + a , 
					 b * ( x*sinTn + y*cosTn )     ) 

createPictureFile(imgWidth , imgHeight , imgType , fileName , drawProc )
