// Canvas handling class

trait Canvas
{
	this: Picture[_] =>
	//
	def isPointVisible(p:(Int,Int)) : Boolean = {
		val (ix,iy) = p
		( ix >= 0 && ix<imgWidth && iy >= 0 && iy < imgHeight )
	}
	//
	def transform( minmax:((Double,Double),(Double,Double)) ):((Double,Double))=>(Int,Int)={
		// println("minmax = "+minmax)
		val (minXY,maxXY) = minmax
		val (minx ,miny)  = minXY
		val (maxx ,maxy)  = maxXY
		val width         = Math.abs( maxx - minx ) + 0.000001
		val height        = Math.abs( maxy - miny ) + 0.000001
		//
		val aspectRatioOfData   = width / height 
		//
		val ratio=	if (aspectRatioOfData < aspectRatio)	imgHeight.asInstanceOf[Double] / height
					else									imgWidth .asInstanceOf[Double] / width
		
		val (offsetX,offsetY)=if( aspectRatioOfData < aspectRatio  ){
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
			val (x,y) = p
			(			(  x*ratio ).asInstanceOf[Int] + offsetX, 
			imgHeight -	( (y*ratio ).asInstanceOf[Int] + offsetY ) ) }
	}
}
