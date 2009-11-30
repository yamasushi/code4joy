// Canvas handling class

trait Canvas
{
	this: Picture[_] =>
	//
	def isPointVisible(p:Vector[Int]) : Boolean = imgFrame.isInside(p)
	//
	def transform( fr:Frame[Double] ):(Vector[Double])=>Vector[Int]={
		val geom  = fr.max operate(fr.min,{ (l,r) => Math.abs(l-r)+0.000001 } )
		//
		val aspectRatioOfData   = geom.x / geom.y 
		//
		val ratio=	if (aspectRatioOfData < aspectRatio)	imgHeight.asInstanceOf[Double] / geom.y
					else									imgWidth .asInstanceOf[Double] / geom.x
		
		val offset:Vector[Double]=if( aspectRatioOfData < aspectRatio  ){
			//data     canvas
			// **      *****
			// ** ---> *****
			// **      *****
			(	- (fr.min.x*ratio) + (imgWidth - (geom.x*ratio))*0.5 ,
				- (fr.min.y*ratio) )
		}
		else {
			//data     canvas
			// *****      **
			// ***** ---> **
			// *****      **
			(	- (fr.min.x*ratio) ,
				- (fr.min.y*ratio) + (imgHeight - (geom.y*ratio))*0.5 )
		}
		// println("ratio = "+ratio+" , (offsetX,offsetY)="+(offsetX,offsetY))
		
		{ p =>
			Vector(	( p.x*ratio + offset.x).asInstanceOf[Int], 
					(-p.y*ratio - offset.y + imgHeight).asInstanceOf[Int] ) }
	}
}
