trait Vector[T]
{
	def apply(i:Int):T
	lazy val x = apply(0)
	lazy val y = apply(1)
	lazy val z = apply(2)
	//
	def map[S](op:T=>S) : Vector[S] = {
		val src = this
		new Vector[S] {
			def apply(i:Int) = op( src.apply(i) )
		}
	}
	//
	def operate(v:Vector[T],op:(T,T)=>T) : Vector[T] = {
		val left  = this
		val right = v
		//
		new Vector[T]{
			def apply(i:Int) = op( left(i) , right(i) )
		}
	}
	//
	def metric(v:Vector[T],binOp:(T,T)=>T,foldOp:Vector[T]=>T) : T = {
		foldOp( operate(v,binOp) )
	}
	//
}

object Vector
{
	def apply[T,S<%T](t:(S,S)  ) : Vector[T] = t2v(t)
	def apply[T,S<%T](t:(S,S,S)) : Vector[T] = t2v(t)
	//
	def apply[T](xx:T,yy:T)            : Vector[T] = t2v((xx,yy)  )
	def apply[T](xx:T,yy:T,zz:T)       : Vector[T] = t2v((xx,yy,zz))
	def apply[T](xx:T,yy:T,zz:T,xs:T*) : Vector[T] = new Vector[T] {
		def apply(i:Int):T = i match {
			case 0 => xx
			case 1 => yy
			case 2 => zz
			case j if ( j >= 3 ) => xs(i-3)
		}
		override def toString = "Vector( "+this(0)+" , "+this(1)+" , "+this(2)+" ,... )"
	}
	//
	implicit def w2v[T,S<%T]( v:{ def apply(i:Int):S } ) : Vector[T] = new Vector[T]{
		def apply(i:Int) = v(i)
	}
	
	implicit def v2t2[T,S<%T](v:Vector[S]) : (T,T)   = (v.x , v.y)
	implicit def v2t3[T,S<%T](v:Vector[S]) : (T,T,T) = (v.x , v.y , v.z)
	
	implicit def t2v[T,S<%T](v:(S,S)) : Vector[T] = new Vector[T]{
		def apply(i:Int):T = i match {
			case 0 => v._1
			case 1 => v._2
		}
		override def toString = "Vector( "+this(0)+" , "+this(1)+" )"
	}
	
	implicit def t2v[T,S<%T](v:(S,S,S)) : Vector[T] = new Vector[T]{
		def apply(i:Int):T = i match {
			case 0 => v._1
			case 1 => v._2
			case 2 => v._3
		}
		override def toString = "Vector( "+this(0)+" , "+this(1)+" , "+this(2)+" )"
	}
	//trigonometirical function
	val trig = Vector(Math.cos _ , Math.sin _)
	//
}
	