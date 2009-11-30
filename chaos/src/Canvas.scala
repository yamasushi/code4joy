// Canvas handling class

trait Canvas
{
	this: Picture[_] =>
	//
	def isPointVisible(p:Vector[Int]) : Boolean = {
		( p.x >= 0 && p.x<imgWidth && p.y >= 0 && p.y < imgHeight )
	}
	//
	def transform( fr:Frame[Double] ):(Vector[Double])=>Vector[Int]={
		val (minx ,miny)  = (fr.min.x , fr.min.y)
		val (maxx ,maxy)  = (fr.max.x , fr.max.y)
		val width         = Math.abs( maxx - minx ) + 0.000001
		val height        = Math.abs( maxy - miny ) + 0.000001
		//
		val aspectRatioOfData   = width / height 
		//
		val ratio=	if (aspectRatioOfData < aspectRatio)	imgHeight.asInstanceOf[Double] / height
					else									imgWidth .asInstanceOf[Double] / width
		
		val offset:Vector[Int]=if( aspectRatioOfData < aspectRatio  ){
			//data     canvas
			// **      *****
			// ** ---> *****
			// **      *****
			(	- (minx*ratio).asInstanceOf[Int] + (imgWidth - (width*ratio)).asInstanceOf[Int] / 2 ,
				- (miny*ratio).asInstanceOf[Int] )
		}
		else {
			//data     canvas
			// *****      **
			// ***** ---> **
			// *****      **
			(	- (minx*ratio).asInstanceOf[Int] ,
				- (miny*ratio).asInstanceOf[Int] + (imgHeight - (height*ratio)).asInstanceOf[Int] / 2 )
		}
		// println("ratio = "+ratio+" , (offsetX,offsetY)="+(offsetX,offsetY))
		
		{ p =>
			Vector(	( p.x*ratio + offset.x).asInstanceOf[Int], 
					(-p.y*ratio - offset.y + imgHeight).asInstanceOf[Int] ) }
	}
}
