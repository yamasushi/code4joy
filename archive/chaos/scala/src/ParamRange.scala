object ParamRange
{
	def apply(start:Double,end:Double,step:Double) : Stream[Double] = {
		val boundary : (Double) => Boolean = if(step<0) { _ >= end } else { _ <= end }
		from( start , step ) takeWhile boundary
	}
	
	def from(start:Double,step:Double) : Stream[Double] = {
		if  (step==0)
			Stream.cons(start,Stream.empty)
		else
			Stream.cons(start,from(start+step,step))
	}
	
	def neighbor(t:Double,tStep:Double,nRadius :Int) : Stream[Double] = {
		val zero   = from(t, 0)
		val plus   = (from(t,+tStep) drop 1 ) take nRadius
		val minus  = (from(t,-tStep) drop 1 ) take nRadius
		Stream.concat( zero , plus , minus )
	}
}
