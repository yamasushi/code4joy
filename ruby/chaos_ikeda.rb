# ikeda attractor
# a.pngに出力

include Java

def createPictureFile(imgWidth , imgHeight , imgType , fileName , &op)
	bi  = java.awt.image.BufferedImage.new( imgWidth , imgHeight , java.awt.image.BufferedImage::TYPE_BYTE_GRAY )
	g   = bi.createGraphics()
	g.setPaint(java.awt.Color::WHITE)
	g.fillRect(0,0,imgWidth,imgHeight)
	g.setColor(java.awt.Color::BLACK)
	
	yield(g)
	
	outFile	= java.io.File.new(fileName)
	javax.imageio.ImageIO.write(bi,imgType,outFile)
end

class Point
	attr_accessor :x , :y
	def initialize(x,y)
		@x = x
		@y = y
	end
end

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

limitPositive = 10.0e6
limitNegative =-limitPositive

createPictureFile(imgWidth , imgHeight , imgType , fileName){ |g|
	rand = java.util.Random.new()
	num_trajectory.times{|j|
		theta= rand.nextDouble * Math::PI * 2
		pt = Point.new( Math::cos( theta ) , Math::sin( theta ) )
		num_point_per_trajectory.times{|i|
			x  = pt.x
			y  = pt.y
			break if (limitNegative > x or limitPositive < x  or limitNegative > y or limitPositive < y)
			
			#print pt.x ," , ", pt.y , "\n"
			
			ix =               ratio*x + offsetx
			iy = imgHeight - ( ratio*y + offsety )
			g.drawLine(ix,iy,ix,iy)
			#
			tn = k - p/(1.0 + (x*x + y*y) )
			#
			sinTn = Math::sin(tn)
			cosTn = Math::cos(tn)
			#
			pt.x = b * ( x*cosTn - y*sinTn ) + a
			pt.y = b * ( x*sinTn + y*cosTn ) 
			#
		}
	}
}
