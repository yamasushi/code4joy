# picure file handling class
# 2009-09-17 shuji yamamoto

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

