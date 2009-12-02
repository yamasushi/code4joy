// Canvas handling class

trait Canvas
{
	this: Picture[_] =>
	//
	def isPointVisible(p:Vector[Int]) : Boolean = geom.frame.isInside(p)
	//
	def transform( dataGeom:Geometry[Double] ):(Vector[Double])=>Vector[Int]={
		//
		val ratio=	if (dataGeom.aspectRatio < geom.aspectRatio)
						geom.size.y.asInstanceOf[Double] / dataGeom.size.y
					else
						geom.size.x.asInstanceOf[Double] / dataGeom.size.x
		
		val offset:Vector[Double]=if( dataGeom.aspectRatio < geom.aspectRatio  ){
			//data     canvas
			// **      *****
			// ** ---> *****
			// **      *****
			(	- (dataGeom.frame.min.x*ratio) + (geom.size.x - (dataGeom.size.x*ratio))*0.5 ,
				- (dataGeom.frame.min.y*ratio) )
		}
		else {
			//data     canvas
			// *****      **
			// ***** ---> **
			// *****      **
			(	- (dataGeom.frame.min.x*ratio) ,
				- (dataGeom.frame.min.y*ratio) + (geom.size.y - (dataGeom.size.y*ratio))*0.5 )
		}
		// println("ratio = "+ratio+" , (offsetX,offsetY)="+(offsetX,offsetY))
		
		{ p =>
			Vector(	( p.x*ratio + offset.x).asInstanceOf[Int], 
					(-p.y*ratio - offset.y + geom.size.y).asInstanceOf[Int] ) }
	}
}
