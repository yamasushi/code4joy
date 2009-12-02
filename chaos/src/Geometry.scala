trait Geometry[T]
{
	val aspectRatio:Double
	val size:Vector[T]
	val frame:Frame[T]
}

object Geometry
{
	def apply(fr:Frame[Double]) : Geometry[Double] = {
		new Geometry[Double]{
			val frame = fr
			val size  = fr.max operate(fr.min,{ (l,r) => Math.abs(l-r)+0.000001 } )
			val aspectRatio = size.x / size.y
		}
	}
	
	def apply(dim:Vector[Int]) : Geometry[Int] = {
		new Geometry[Int]{
			val frame = Frame[Int]( Vector(0,0) , Vector(dim.x-1,dim.y-1) )
			val size  = dim
			val aspectRatio = size.x.asInstanceOf[Double] / size.y.asInstanceOf[Double]
		}
	}
}
