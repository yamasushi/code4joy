# sprott attractor
# output to a.png
# identify attractor as 12 charactor string
# as FIRCDERRPVLD
#
# for more detail , see :
#   http://sprott.physics.wisc.edu/pubs/paper203.htm

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

# coefficients named A through Y (25 possible values)
coef ={	'A':-1.2,
		'B':-1.1,
		'C':-1.0,
		'D':-0.9,
		'E':-0.8,
		'F':-0.7,
		'G':-0.6,
		'H':-0.5,
		'I':-0.4,
		'J':-0.3,
		'K':-0.2,
		'L':-0.1,
		'M': 0.0, # <---- Zero
		'N':+0.1,
		'O':+0.2,
		'P':+0.3,
		'Q':+0.4,
		'R':+0.5,
		'S':+0.6,
		'T':+0.7,
		'U':+0.8,
		'V':+0.9,
		'W':+1.0,
		'X':+1.1,
		'Y':+1.2}

#		123456123456
sign = "FIRCDERRPVLD" # 12charactors signature of system 
#"AMTMNQQXUYGA"
#"CVQKGHQTPNTE"

a = map(lambda x:coef[x],sign)
print a

num_trajectory           = 100
num_point_per_trajectory = 1000

ratio     = 200.0
imgWidth  = 500
imgHeight = 500
offsetx   = imgWidth /2
offsety   = imgHeight/2
imgType   = "png"
fileName  = "a.png"

oval_r    = 0.1
num_ring  = 1

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
			
			x_x  = x*x
			y_y  = y*y
			x_y  = x*y
			(x,y) = (	a[0] + a[1]*x + a[2]*x_x + a[3]*x_y + a[ 4]*y + a[ 5]*y_y ,
						a[6] + a[7]*x + a[8]*x_x + a[9]*x_y + a[10]*y + a[11]*y_y )

createPictureFile(imgWidth , imgHeight , imgType , fileName , drawProc )
