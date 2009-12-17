require "lattice"

ImageBuffer = {}
ImageBuffer_mt = {__index = ImageBuffer}

function ImageBuffer:new(img_width,img_height)
	assert(img_width)
	assert(img_height)
	local o    = {}
	o.width    = img_width
	o.height   = img_height
	o.histgram = {}
	--
	o.cell_unit = 5
	o.rx = o.cell_unit
	o.ry = math.sqrt(3)*o.cell_unit
	o.cell_width  = math.floor(o.width /o.rx + 0.5)
	o.cell_height = math.floor(o.height/o.ry + 0.5)
	--
	o.scan_depth  = 4
	o.center_cell = Vector:new(o.cell_width/2,o.cell_height/2)
	--
	return setmetatable(o,ImageBuffer_mt)
end

function ImageBuffer:update(x,y,v,on_update)
	assert(self)
	assert(x)
	assert(y)
	assert(v)
	assert(on_update)
	assert(self.histgram)
	assert(self.width)
	assert(self.height)
	--
	if( x <  0 ) then return 0 end
	if( x >= self.width ) then return 0 end
	if( y <  0 ) then return 0 end
	if( y >= self.height ) then return 0 end
	--
	local ix = math.floor(x/self.rx +0.5)
	local iy = math.floor(y/self.ry +0.5)
	--print(x,y)
	return self:update_cell(ix,iy,v,on_update)
end

function ImageBuffer:update_cell(ix,iy,v,on_update)
	assert(self)
	assert(ix)
	assert(iy)
	assert(v)
	assert(on_update)
	assert(self.histgram)
	assert(self.width)
	assert(self.height)
	--
	if( ix <  0 ) then return 0 end
	if( ix >= self.cell_width  ) then return 0 end
	if( iy <  0 ) then return 0 end
	if( iy >= self.cell_height ) then return 0 end
	--
	if (self.histgram[ix] == nil) then
		self.histgram[ix]= {}
	end
	if(on_update)then on_update(v) end
	self.histgram[ix][iy] = v
	return v
end

function ImageBuffer:cell(ix,iy)
	assert(self)
	assert(ix)
	assert(iy)
	assert(self.histgram)
	assert(self.width)
	assert(self.height)
	--
	if( ix <  0 ) then return 0 end
	if( ix >= self.cell_width ) then return 0 end
	if( iy <  0 ) then return 0 end
	if( iy >= self.cell_height ) then return 0 end
	--
	if( self.histgram[ix] == nil ) then return 0 end
	if( self.histgram[ix][iy] == nil ) then return 0 end
	--
	return self.histgram[ix][iy]
end

function ImageBuffer:smooth(cond_op,on_update)
	assert(self)
	assert(cond_op)
	assert(on_update)
	assert(self.histgram)
	assert(self.width)
	assert(self.height)
	for ix = 0,self.cell_width-1 do
		for iy = 0,self.cell_height-1 do
			if( cond_op(self:cell(ix,iy)) ) then
				self:update_cell(
					ix ,
					iy ,
					(	self:cell(ix  ,iy  ) +
						--
						self:cell(ix+2,iy  ) +
						self:cell(ix+1,iy+1) +
						self:cell(ix-1,iy+1) +
						self:cell(ix-2,iy  ) +
						self:cell(ix+1,iy-1) +
						self:cell(ix-1,iy-1) ) / 7.0 , on_update )
			end
		end
	end
end


function ImageBuffer:eachcell(op)
	assert(self)
	assert(self.histgram)
	--
	for iy = 0,self.cell_height-1 do
		local y = iy*self.ry
		local isx = 0
		if( iy%2 == 0 ) then
			isx = isx+3
		end
		for ix = isx , isx + self.cell_width-1 , 6 do
			local x = ix*self.rx
			local h = self:cell(ix,iy)
			op(	{	{x + 2*self.rx , y} ,
					{x +   self.rx , y + self.ry } ,
					{x -   self.rx , y + self.ry } ,
					{x - 2*self.rx , y} ,
					{x -   self.rx , y - self.ry } ,
					{x +   self.rx , y - self.ry }} , h)
		end
	end
end

function ImageBuffer:each_starcell(op)
	assert(self)
	assert(self.histgram)
	--
	local i = 0
	Lattice:scan( self.scan_depth , self.center_cell/2 ,
		function(ip)
			Lattice:star_neighbor(i,ip*2,
				function(r)
					--
					local h = (	self:cell(r[1][1],r[1][2]) +
								self:cell(r[2][1],r[2][2]) +
								self:cell(r[3][1],r[3][2]) +
								self:cell(r[4][1],r[4][2]) ) / 4.0
					op( {	{r[1][1]*self.rx , r[1][2]*self.ry } ,
							{r[2][1]*self.rx , r[2][2]*self.ry } ,
							{r[3][1]*self.rx , r[3][2]*self.ry } ,
							{r[4][1]*self.rx , r[4][2]*self.ry } } , h)
					--
				end )
			i = (i+1)%7
		end )
end
