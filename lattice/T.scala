// Test
import java.awt.{Color,Graphics2D}
import java.io._

val l = Lattice(3 ,2.0)

val pic = new PictureFile(new File("a.png"),Geometry(1000,1000),"png",Color.BLACK)

var sp = l.pos((0,0))
var ep = l.pos((0,0))

pic paint { g =>
	g.setColor(Color.WHITE)
	Lattice.sampling(4,(0,0)) { ip => 
		println( (ip.x,ip.y))
		sp = ep
		ep = l.pos(ip)
		g.drawLine(	sp.x.asInstanceOf[Int] , 
					sp.y.asInstanceOf[Int] , 
					ep.x.asInstanceOf[Int] , 
					ep.y.asInstanceOf[Int] )
	}
}
