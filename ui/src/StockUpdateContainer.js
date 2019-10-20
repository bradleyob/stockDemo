import React, {Component} from 'react'
import StockUpdate from './StockUpdate'

class StockUpdateContainer extends Component {
    render() {
        const stocks = Object.keys(this.props.stockData).map(stock => {
            return <StockUpdate
                symbol={this.props.stockData[stock].symbol}
                name={this.props.stockData[stock].name}
                price={this.props.stockData[stock].price}
                removeStock={this.props.removeStock}/>
        });
        return <div>{stocks}</div>
    }
}
export default StockUpdateContainer