// Canvas handling class

trait Canvas
{
	this: Picture[_] =>
	//
	def isPointVisible(p:Vector[Int]) : Boolean = imgFrame.isInside(p)
	//
	def transform( dataGeom:Geometry ):(Vector[Double])=>Vector[Int]={
		//
		val ratio=	if (dataGeom.aspectRatio < aspectRatio)
						imgHeight.asInstanceOf[Double] / dataGeom.size.y
					else
						imgWidth .asInstanceOf[Double] / dataGeom.size.x
		
		val offset:Vector[Double]=if( dataGeom.aspectRatio < aspectRatio  ){
			//data     canvas
			// **      *****
			// ** ---> *****
			// **      *****
			(	- (dataGeom.frame.min.x*ratio) + (imgWidth - (dataGeom.size.x*ratio))*0.5 ,
				- (dataGeom.frame.min.y*ratio) )
		}
		else {
			//data     canvas
			// *****      **
			// ***** ---> **
			// *****      **
			(	- (dataGeom.frame.min.x*ratio) ,
				- (dataGeom.frame.min.y*ratio) + (imgHeight - (dataGeom.size.y*ratio))*0.5 )
		}
		// println("ratio = "+ratio+" , (offsetX,offsetY)="+(offsetX,offsetY))
		
		{ p =>
			Vector(	( p.x*ratio + offset.x).asInstanceOf[Int], 
					(-p.y*ratio - offset.y + imgHeight).asInstanceOf[Int] ) }
	}
}
