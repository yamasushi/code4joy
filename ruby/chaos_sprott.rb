# sprott attractor
# 12文字のAからYで識別する
# 例 FIRCDERRPVLD
# a.pngに出力
# 詳細は
# http://sprott.physics.wisc.edu/pubs/paper203.htm

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

coef =	{	# coefficients named A through Y (25 possible values)
					'A'=>-1.2,
					'B'=>-1.1,
					'C'=>-1.0,
					'D'=>-0.9,
					'E'=>-0.8,
					'F'=>-0.7,
					'G'=>-0.6,
					'H'=>-0.5,
					'I'=>-0.4,
					'J'=>-0.3,
					'K'=>-0.2,
					'L'=>-0.1,
					'M'=> 0.0, # <---- Zero
					'N'=>+0.1,
					'O'=>+0.2,
					'P'=>+0.3,
					'Q'=>+0.4,
					'R'=>+0.5,
					'S'=>+0.6,
					'T'=>+0.7,
					'U'=>+0.8,
					'V'=>+0.9,
					'W'=>+1.0,
					'X'=>+1.1,
					'Y'=>+1.2
					#
}
		123456123456
sign = 'FIRCDERRPVLD' # 12charactors signature of system 
#"AMTMNQQXUYGA"
#"CVQKGHQTPNTE"

a = sign.scan(/./).map{|x| coef[x] }
p a

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
			
			#
			#print pt.x ," , ", pt.y , "\n"
			ix =               ratio*x + offsetx
			iy = imgHeight - ( ratio*y + offsety )
			g.drawLine(ix,iy,ix,iy)
			#
			x_x  = x*x
			y_y  = y*y
			x_y  = x*y
			#
			pt.x = a[0] + a[1]*x + a[2]*x_x + a[3]*x_y + a[ 4]*y + a[ 5]*y_y   
			pt.y = a[6] + a[7]*x + a[8]*x_x + a[9]*x_y + a[10]*y + a[11]*y_y
			#
		}
	}
}
