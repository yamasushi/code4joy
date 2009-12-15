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
	o.rx = 0.5
	o.ry = math.sqrt(3)*0.5
	o.cell_width  = math.floor(o.width /o.rx + 0.5)
	o.cell_height = math.floor(o.height/o.ry + 0.5)
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

	local current = 0.0
	if (self.histgram[ix] == nil) then
		self.histgram[ix]= {}
	end
	if ( self.histgram[ix][iy] ) then
		current = self.histgram[ix][iy]
	else
		current = 0.0
	end
	local result = (v+current)*0.5
	if(on_update)then on_update(result) end
	self.histgram[ix][iy] = result
	return result
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
	for ix,row in pairs(self.histgram) do
		local x = math.floor( ix*self.rx + 0.5 )
		--print("ix--"..ix)
		for iy,h in pairs(row) do
			local y = math.floor( iy*self.ry + 0.5 )
			op(x,y,h)
		end
	end
end
