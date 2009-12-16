require "Vector"

Matrix = {}
Matrix_mt = {__index = Matrix}
function Matrix:new(u,v)
	assert(u)
	assert(v)
	local o = {Vector:new(u[1],u[2]),Vector:new(v[1],v[2])}
	o.u = o[1]
	o.v = o[2]
	return setmetatable(o,Matrix_mt)
end

Matrix.zero = Matrix:new( {0,0} , {0,0} )
Matrix.unit = Matrix:new( {1,0} , {0,1} )

function Matrix:uv()
	assert(self)
	return self[1] , self[2]
end

function Matrix:transpose()
	assert(self)
	return Matrix:new( Vector:new( self[1][1] , self[2][1] ) , Vector:new( self[1][2] , self[2][2] ) )
end

Matrix_mt.__call= function(a,b) return (a:map())(b) end
Matrix_mt.__unm = function(a)   return Matrix:new( -a[1] , -a[2] ) end
Matrix_mt.__add = function(a,b) return Matrix:new( a[1]+b[1] , a[2]+b[2] ) end
Matrix_mt.__sub = function(a,b) return Matrix:new( a[1]-b[1] , a[2]-b[2] ) end
Matrix_mt.__tostring = function(a) return string.format("(%s,%s)", tostring(a[1]) , tostring(a[2]) ) end

Matrix_mt.__mul = function(a,b)
	if( type(a)=="number" ) then
		return Matrix:new( a*b[1] , a*b[2] )
	end
	if( type(b)=="number" ) then
		return Matrix:new( b*a[1] , b*a[2] )
	end

	local tb = b:transpose()
	return Matrix:new(
			Vector:new( Vector:dot(a[1],tb[1]) , Vector:dot(a[1],tb[2]) ) ,
			Vector:new( Vector:dot(a[2],tb[1]) , Vector:dot(a[2],tb[2]) ) )
end

function Matrix:rotate(t)
	local cos_t = math.cos(t)
	local sin_t = math.sin(t)
	return Matrix:new(
			Vector:new(cos_t,-sin_t),
			Vector:new(sin_t, cos_t))
end

function Matrix:scale(x,y)
	return Matrix:new(
			Vector:new(x,0),
			Vector:new(0,y))
end


function Matrix:map()
	assert(self)
	local o = self
	return function(t)
		--print (o[1],o[2])
		local x = Vector:dot(o[1] , t)
		local y = Vector:dot(o[2] , t)
		--print(x,y)
		return Vector:new( x , y )
	end
end
