trait Geometry
{
	val aspectRatio:Double
	val size:Vector[Double]
	val frame:Frame[Double]
}

object Geometry
{
	def apply(fr:Frame[Double]) : Geometry = {
		new Geometry{
			val frame = fr
			val size  = frame.max operate(frame.min,{ (l,r) => Math.abs(l-r)+0.000001 } )
			val aspectRatio = size.x / size.y
		}
	}
}
