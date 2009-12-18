-- Hexagonal Lattce
require "Matrix"
require "Vector"

Lattice = {}
Lattice_mt = {__index = Lattice}

function Lattice:new(ratio_root,unit)
	local o = {}
	o.ratio_root = ratio_root
	o.ratio = math.sqrt(ratio_root)
	o.unit  = unit
	return setmetatable(o,Lattice_mt)
end

Lattice_mt.__tostring = function(a) return string.format("Lattice(ratio=%f(=sqrt(%d)),unit=%f)",
															a.ratio,a.ratio_root,a.unit) end

Lattice.scaler = Matrix:new({-2,3},{1,2})
Lattice.scaler_cache = { [0]=Matrix.unit , [1]=Lattice.scaler }

function Lattice:pos(ip)
	assert(self)
	return Vector:new(ip[1] , ip[2]*self.ratio) * self.unit
end

function Lattice:neighbor(ip)
	local ix,iy = ip[1],ip[2]
	return	{	{ix + 2 , iy     } ,
				{ix + 1 , iy - 1 } ,
				{ix - 1 , iy - 1 } ,
				{ix - 2 , iy     } ,
				{ix - 1 , iy + 1 } ,
				{ix + 1 , iy + 1 } }
end

function Lattice:star_shape_0()
	local star={}
	star.inner = {
		{{ 0, 0},{ 2, 0},{ 3, 1},{ 1, 1}} ,
		{{ 0, 0},{ 1, 1},{ 0, 2},{-1, 1}} ,
		{{ 0, 0},{-1, 1},{-3, 1},{-2, 0}} ,
		{{ 0, 0},{-2, 0},{-3,-1},{-1,-1}} ,
		{{ 0, 0},{-1,-1},{ 0,-2},{ 1,-1}} ,
		{{ 0, 0},{ 1,-1},{ 3,-1},{ 2, 0}} }
	--
	star.outer = {
		{{ 4, 0},{ 3, 1},{ 2, 0},{ 3,-1}} ,
		{{ 2, 2},{ 0, 2},{ 1, 1},{ 3, 1}} ,
		{{-2, 2},{-3, 1},{-1, 1},{ 0, 2}} ,
		{{-4, 0},{-3,-1},{-2, 0},{-3, 1}} ,
		{{-2,-2},{ 0,-2},{-1,-1},{-3,-1}} ,
		{{ 2,-2},{ 3,-1},{ 1,-1},{ 0,-2}} }
	return star
end

function Lattice:star_shape(n)
	local r = n%7
	local star = Lattice:star_shape_0()
	if(n==0) then return star end
	--
	local j_in = r
	local i_in = r-1
	if (i_in<=0) then i_in = 6 end

	local i_in_val = { star.outer[j_in][1] , star.inner[i_in][4] , star.inner[i_in][2] , star.inner[i_in][3] }
	local j_in_val = { star.outer[j_in][1] , star.inner[j_in][3] , star.inner[j_in][4] , star.inner[j_in][2] }
	local j_out_val= { {0,0} , star.inner[i_in][2] , star.outer[j_in][3] , star.inner[j_in][4] }

	star.inner[j_in] , star.inner[i_in] , star.outer[j_in]= j_in_val , i_in_val , j_out_val

	return star
end

Lattice.star = {}
Lattice.star[0] = Lattice:star_shape(0)
Lattice.star[1] = Lattice:star_shape(1)
Lattice.star[2] = Lattice:star_shape(2)
Lattice.star[3] = Lattice:star_shape(3)
Lattice.star[4] = Lattice:star_shape(4)
Lattice.star[5] = Lattice:star_shape(5)
Lattice.star[6] = Lattice:star_shape(6)

-- op rhombus -->
function Lattice:star_neighbor(n,ip,op)
	local star = Lattice.star[ n%7 ]
	local x,y = ip[1] , ip[2]
	for i,r in ipairs(star.outer) do
		local v={}
		table.insert(v,Vector:new( r[1][1]+x , r[1][2]+y ) )
		table.insert(v,Vector:new( r[2][1]+x , r[2][2]+y ) )
		table.insert(v,Vector:new( r[3][1]+x , r[3][2]+y ) )
		table.insert(v,Vector:new( r[4][1]+x , r[4][2]+y ) )
		op(v)
	end
	for i,r in ipairs(star.inner) do
		local v={}
		table.insert(v,Vector:new( r[4][1]+x , r[4][2]+y ) )
		table.insert(v,Vector:new( r[1][1]+x , r[1][2]+y ) )
		table.insert(v,Vector:new( r[2][1]+x , r[2][2]+y ) )
		table.insert(v,Vector:new( r[3][1]+x , r[3][2]+y ) )
		op(v)
	end
end

function Lattice:nth_scaler(n)
	assert(n)
	if( n < 0 ) then return Matrix.unit end
	local nth = Lattice.scaler_cache[n]
	if( nth ) then return nth end
	--
	nth = Lattice:nth_scaler(n-1) * Lattice.scaler
	Lattice.scaler_cache[n] = nth
	return nth
end

function Lattice:scaling(n)
	assert(n)
	return Lattice:nth_scaler(n):map()
end

function Lattice:scan(n,ip,op)
	assert(n)
	assert(ip)
	assert(op)
	Lattice:sampling(
			n  ,
			ip ,
			function(t) op(t) return 0 end ,
			function(t) return 0 end )
end


function Lattice:sampling(n,ip,get_op,acc_op)
	assert(n)
	assert(ip)
	assert(get_op)
	assert(acc_op)

	local p = Vector:new(ip[1],ip[2])
	if( n<0 ) then
		return get_op(p)
	end

	local map = Lattice:scaling(n)
	local u = map( { 3 , -1 } )
	local v = map( { 0 , -2 } )
	local w = map( {-3 , -1 } )

	local param = {}
	--print("0---",n,p)
	table.insert( param , Lattice:sampling(n-1 , p , get_op , acc_op) )

	p = p - v
	--print("1---",n,p)
	table.insert( param , Lattice:sampling(n-1 , p , get_op , acc_op) )

	p = p + u
	--print("2---",n,p)
	table.insert( param , Lattice:sampling(n-1 , p , get_op , acc_op) )

	p = p + v
	--print("3---",n,p)
	table.insert( param , Lattice:sampling(n-1 , p , get_op , acc_op) )

	p = p + w
	--print("4---",n,p)
	table.insert( param , Lattice:sampling(n-1 , p , get_op , acc_op) )

	p = p - u
	--print("5---",n,p)
	table.insert( param , Lattice:sampling(n-1 , p , get_op , acc_op) )

	p = p - v
	--print("6---",n,p)
	table.insert( param , Lattice:sampling(n-1 , p , get_op , acc_op) )

	return acc_op(param)

end
