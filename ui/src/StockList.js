import React, {Component} from 'react'

class StockList extends Component {
    componentDidUpdate() {
        this.props.inputElement.current.focus()
    }
    render() {
        return (
            <div className="stockList">
                <div className="header">
                    <form onSubmit={this.props.addStock}>
                        <input placeholder="Symbol"
                               ref={this.props.inputElement}
                               value={this.props.currentItem.text}
                               onChange={this.props.handleInput}
                        />
                        <button type="submit"> Add Stock </button>
                    </form>
                </div>
            </div>
        )
    }
}
export default StockList