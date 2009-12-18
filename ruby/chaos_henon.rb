# henon attractor
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

# from : http://codezine.jp/article/detail/327?p=2

h_sq =proc{|x|x*x}
h_cu =proc{|x|x*x*x}
h_si =proc{|x|0.8*Math::sin(x)}
h_in =proc{|x|0.1 / x}

henon = h_sq
alpha = 0.47 * Math::PI
# 0.07
# 0.30
# 0.39
# 0.47
# 0.61
# 0.84
sin_alpha = Math::sin(alpha)
cos_alpha = Math::cos(alpha)

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

limitPositive = 10.0e6
limitNegative =-limitPositive

createPictureFile(imgWidth , imgHeight , imgType , fileName){ |g|
	rand = java.util.Random.new()
	num_trajectory.times{|j|
		iring = (j % num_ring) + 1 
		r     = (oval_r * iring)/num_ring
		theta = rand.nextDouble * Math::PI * 2
		#
		pt = Point.new( r * Math::cos( theta ) , r * Math::sin( theta ) )
		#
		num_point_per_trajectory.times{|i|
			x  = pt.x
			y  = pt.y
			break if (limitNegative > x or limitPositive < x  or limitNegative > y or limitPositive < y)
			
			#print pt.x ," , ", pt.y , "\n"
			ix =               ratio*x + offsetx
			iy = imgHeight - ( ratio*y + offsety )
			g.drawLine(ix,iy,ix,iy)
			#
			temp  = y - henon.call(x)
			pt.x = x*cos_alpha - temp*sin_alpha  
			pt.y = x*sin_alpha + temp*cos_alpha 
			#
		}
	}
}
