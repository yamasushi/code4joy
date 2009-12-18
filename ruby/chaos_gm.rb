# Gumowski-Mira attractor
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

xa   =  0.0005
xb   =  1.0
a    = -0.48
b    =  0.9924

f_0 = proc{|x|
		x_x = x*x
		a * x + ( 2*(1-a)*x_x / (1+x_x) );
	}

f_1 = proc{|x|
		x_x = x*x
		a * x + ( 2*(1-a)*x_x / ((1+x_x)*(1+x_x)) );
	}
	
# from http://codezine.jp/article/detail/327?p=2
f_2 = proc{|x|
		x_x = x*x
		a * x + ( 2*(1-a)*x / (1+x_x) );
	}
	
# from http://codezine.jp/article/detail/327?p=2
f_3 = proc{|x|
		x_x = x*x
		a * x + ( 2*(1-a) / (1+x_x) );
	}

g_0 = proc{|y|
		b *y + xa * (1- xb*y*y)*y
	}

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
		u = gm_f.call(pt.x)
		num_point_per_trajectory.times{|i|
			x  = pt.x
			y  = pt.y
			break if (limitNegative > x or limitPositive < x  or limitNegative > y or limitPositive < y)
			
			#print pt.x ," , ", pt.y , "\n"
			ix =               ratio*x + offsetx
			iy = imgHeight - ( ratio*y + offsety )
			g.drawLine(ix,iy,ix,iy)
			#
			xx = gm_g.call(y) + u;
			u  = gm_f.call(xx)
			yy = u - x;
			//
			pt.x = xx
			pt.y = yy
		}
	}
}
