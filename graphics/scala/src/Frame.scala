
case class Frame[T <% Ordered[T]](min:Vector[T],max:Vector[T])
{
	def isInside( pt:Vector[T] ) : Boolean = {
		(	pt.x >= min.x && pt.x <= max.x &&
			pt.y >= min.y && pt.y <= max.y )
	}
	//
	def inflate( p:Vector[T] ) : Frame[T] ={
		Frame(	(if(p.x < min.x) p.x else min.x , if(p.y < min.y)p.y else min.y) ,
				(if(p.x > max.x) p.x else max.x , if(p.y > max.y)p.y else max.y) )
	}
	//
	def inflate( p:Frame[T] ) : Frame[T] ={
		inflate(p.min).inflate(p.max)
	}
}

