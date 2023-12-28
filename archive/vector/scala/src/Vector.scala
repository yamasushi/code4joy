import java.awt.{Color,Point}

// !!! scala 2.8 has "Vector" collection. be aware  !!!

trait Vector[T]
{
	import Vector._
	//
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
	def operate(v:Vectorical[T] , op:(T,T)=>T) : Vector[T] = {
		val left  = this
		val right = v
		//
		new Vector[T]{
			def apply(i:Int) = op( left(i) , right(i) )
		}
	}
	//
	def metric(v:Vectorical[T],binOp:(T,T)=>T,foldOp:Vectorical[T]=>T) : T = {
		foldOp( operate(v,binOp) )
	}
	//
}

object Vector
{
	type Vectorical[T] = {def apply(i:Int):T}
	//
	def apply[T,S<%T](t:(S,S)  ) : Vector[T] = t2v(t)
	def apply[T,S<%T](t:(S,S,S)) : Vector[T] = t2v(t)
	//
	def apply[T](xx:T,yy:T)           : Vector[T] = t2v((xx,yy)  )
	def apply[T](xx:T,yy:T,zz:T)      : Vector[T] = t2v((xx,yy,zz))
	def apply[T](xx:T,yy:T,zz:T,ww:T) : Vector[T] = t2v((xx,yy,zz,ww))
	def apply[T](xx:T,yy:T,zz:T,ww:T,xs:T*) = new Vector[T] {
		def apply(i:Int):T = i match {
			case 0 => xx
			case 1 => yy
			case 2 => zz
			case 3 => ww
			case j if ( j >= 4 ) => xs(i-4)
		}
		override def toString = "Vector("+this(0) +
									","+this(1) + 
									","+this(2) +
									","+this(3) + " ,... )"
	}
	//
	implicit def w2v[T,S<%T]( v:Vectorical[S] ) = new Vector[T]{
		def apply(i:Int) = v(i)
	}
	//
	implicit def w2t2[T](v:Vectorical[T]) = (v(0) , v(1))
	implicit def w2t3[T](v:Vectorical[T]) = (v(0) , v(1) , v(2))
	//
	implicit def color2v(c:Color) = new Vector[Double]{
		def apply(i:Int) = i match {
			case 0 => c.getRed   / 255.0
			case 1 => c.getGreen / 255.0
			case 2 => c.getBlue  / 255.0
			case 3 => c.getAlpha / 255.0
		}
	}
	//
	implicit def v2color(v:Vectorical[Double]):Color = {
		if ( v(3)==1.0 )
			new Color(	(v(0)*255).asInstanceOf[Int] , 
						(v(1)*255).asInstanceOf[Int] ,
						(v(2)*255).asInstanceOf[Int] )
		else
			new Color(	(v(0)*255).asInstanceOf[Int] , 
						(v(1)*255).asInstanceOf[Int] ,
						(v(2)*255).asInstanceOf[Int] ,
						(v(3)*255).asInstanceOf[Int] )
	}
	//
	implicit def point2v(p:Point):Vector[Int]=t2v( p.getX.asInstanceOf[Int] , p.getY.asInstanceOf[Int] )
	//
	implicit def v2point(v:Vectorical[Int]   ):Point = new Point(v(0),v(1))
	//
	implicit def t2v[T,S<%T](v:(S,S)) : Vector[T] = new Vector[T]{
		def apply(i:Int):T = i match {
			case 0 => v._1
			case 1 => v._2
		}
		override def toString = "Vector("+this(0)+","+this(1)+")"
	}
	
	implicit def t2v[T,S<%T](v:(S,S,S)) = new Vector[T]{
		def apply(i:Int):T = i match {
			case 0 => v._1
			case 1 => v._2
			case 2 => v._3
		}
		override def toString = "Vector("+this(0)+","+this(1)+","+this(2)+")"
	}
	
	implicit def t2v[T,S<%T](v:(S,S,S,S)) = new Vector[T]{
		def apply(i:Int):T = i match {
			case 0 => v._1
			case 1 => v._2
			case 2 => v._3
			case 3 => v._4
		}
		override def toString = "Vector("+this(0)+","+this(1)+","+this(2)+","+this(3)+")"
	}
	
	//trigonometirical function
	val trig = Vector(Math.cos _ , Math.sin _)
	//
}
	
