import React from 'react'

export default ({ symbol, name, price, removeStock}) =>
    <div className='stock' ref={ React.createRef() } >
        <strong>{symbol}</strong> <strong>{name}</strong> <em>{price}</em><button onClick = {() => removeStock(symbol)} title={"Remove " + symbol}/>
    </div>