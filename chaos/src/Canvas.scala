// Canvas handling class

trait Canvas
{
	this: Picture[_] =>
	//
	def isPointVisible(p:Vector[Int]) : Boolean = imgFrame.isInside(p)
	//
	def transform( fr:Frame[Double] ):(Vector[Double])=>Vector[Int]={
		val width         = Math.abs( fr.max.x - fr.min.x ) + 0.000001
		val height        = Math.abs( fr.max.y - fr.min.y ) + 0.000001
		//
		val aspectRatioOfData   = width / height 
		//
		val ratio=	if (aspectRatioOfData < aspectRatio)	imgHeight.asInstanceOf[Double] / height
					else									imgWidth .asInstanceOf[Double] / width
		
		val offset:Vector[Double]=if( aspectRatioOfData < aspectRatio  ){
			//data     canvas
			// **      *****
			// ** ---> *****
			// **      *****
			(	- (fr.min.x*ratio) + (imgWidth - (width*ratio))*0.5 ,
				- (fr.min.y*ratio) )
		}
		else {
			//data     canvas
			// *****      **
			// ***** ---> **
			// *****      **
			(	- (fr.min.x*ratio) ,
				- (fr.min.y*ratio) + (imgHeight - (height*ratio))*0.5 )
		}
		// println("ratio = "+ratio+" , (offsetX,offsetY)="+(offsetX,offsetY))
		
		{ p =>
			Vector(	( p.x*ratio + offset.x).asInstanceOf[Int], 
					(-p.y*ratio - offset.y + imgHeight).asInstanceOf[Int] ) }
	}
}
