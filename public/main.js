var ws = new ReconnectingWebSocket("ws://localhost:8080/domain/socket");
ws.onopen = function () {
    console.log("opened ws");
};
var eventHandlers = {};
function invokeHandlers(message) {
    $.each(eventHandlers[message.event], function (_, handler) {
        console.log(handler);
        handler(message);
    })
}
function subscribe(event, handler) {
    if(!$.isArray(eventHandlers[event])) eventHandlers[event] = [];
    console.log(handler);
    eventHandlers[event].push(handler);
}
ws.onmessage = function (message) {
    var parse = JSON.parse(message.data);
    if($.isArray(eventHandlers[parse.event])) {
        invokeHandlers(parse);
    }
};

var NewUserButton = React.createClass({
    render: function() {
        return (
            <button type="submit">Add User</button>
        );
    }
});

var NewProductButton = React.createClass({
    render: function() {
        return (
            <button>Add Product</button>
        );
    }
});

var ToUserPageButton = React.createClass({
    handleClick: function() {
        ReactDOM.unmountComponentAtNode(document.getElementById('content'));
        ReactDOM.render(<UserPage />, document.getElementById('content'));
    },
    render: function() {
        return (
            <button onClick={this.handleClick}>User List</button>
        );
    }
});

var UserWithBalance = React.createClass({
    subtractBalance: function(message) {
        if(message.id === this.props.account.id) {
            this.props.account.balance -= message.amount;
            this.setState({account: this.props.account});
        }
    },
    addBalance: function(message) {
        if(message.id === this.props.account.id) {
            this.props.account.balance += message.amount;
            this.setState({account: this.props.account});
        }
    },
    componentDidMount: function() {
        subscribe('AccountDebitedEventWebsocketMessage', this.subtractBalance);
        subscribe('AccountCreditedEventWebsocketMessage', this.addBalance);
    },
    render: function() {
        return (
            <div>
                <Account account={this.props.account} />
                {this.props.account.balance}
            </div>
        );
    }
});

var AccountFormNameInput = React.createClass({
    handleChange: function(event) {
        this.setState({name: event.target.value});
    },
    render: function () {
        return (
            <input type="text" placeholder="name" name="name" onChange={this.handleChange} />
        );
    }
});
var UserAccountEmailFormInput = React.createClass({
    handleChange: function(event) {
        this.setState({email: event.target.value});
    },
    render: function() {
        return (
            <input type="email" placeholder="email" name="email" onChange={this.handleChange} />
        );
    }
});
var ProductAccountPriceFormInput = React.createClass({
    handleChange: function(event) {
        this.setState({price: event.target.value});
    },
    render: function() {
        return (
            <input type="text" placeholder="price" name="fixedAmount" onChange={this.handleChange} />
        );
    }
});

var ProductAccountForm = React.createClass({
    handleOnSubmit: function(event) {
        event.preventDefault();
        $.ajax('/domain/accounts/product', {
            method: 'POST',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify({fixedAmount:this.refs.price.state.price, name:this.refs.name.state.name})
        });
    },
    render: function() {
        return (
            <form onSubmit={this.handleOnSubmit}>
                <div>
                    <AccountFormNameInput ref="name" />
                    <ProductAccountPriceFormInput ref="price" />
                    <NewProductButton />
                </div>
            </form>
        );
    }
});

var UserAccountForm = React.createClass({
    handleOnSubmit: function(event) {
        event.preventDefault();
        $.ajax('/domain/accounts/user', {
            method: 'POST',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify({email:this.refs.email.state.email, name:this.refs.name.state.name})
        });
    },
    render: function () {
        return (
            <form onSubmit={this.handleOnSubmit}>
                <div>
                    <AccountFormNameInput ref="name" />
                    <UserAccountEmailFormInput ref="email" />
                    <NewUserButton />
                </div>
            </form>
        );
    }
});

var currentAccount;
var Account = React.createClass({
    handleUserClick: function() {
        currentAccount = this.props.account;
        ReactDOM.unmountComponentAtNode(document.getElementById('content'));
        ReactDOM.render(<ProductPage />, document.getElementById('content'));
    },
    handleProductClick: function() {
        $.ajax('/domain/accounts/transaction', {
            method: 'POST',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify({fromAccount:currentAccount.id, toAccount: this.props.account.id})
        });
    },
    handleClick: function () {
        if($.isNumeric(this.props.account.fixedAmount)) {
            this.handleProductClick();
        }
        else {
            this.handleUserClick();
        }
    },
    render: function() {
        var priceTag = '';
        if($.isNumeric(this.props.account.fixedAmount)) {
            priceTag = <div className="price">€ {this.props.account.fixedAmount}</div>;
        }
        return (
            <div id="account_{this.props.account.id}" className="account" onClick={this.handleClick}>
                <div className="image"><img src="http://dummyimage.com/90" alt="{this.props.account.name}s avatar" /></div>
                <div className="name">{this.props.account.name}</div>
                {priceTag}
            </div>
        );
    }
});

var AccountList = React.createClass({
    getInitialState: function() {
        return {data: []};
    },
    loadAccountList: function() {
        if(this.isMounted()) {
            $.ajax({
                url: this.props.url,
                dataType: 'json',
                cache: false,
                success: function(data) {
                    this.setState({data: data});
                }.bind(this),
                error: function(xhr, status, err) {
                    console.error(this.props.url, status, err.toString());
                }.bind(this)
            });
        }
    },
    appendAccount: function (account) {
        if(this.isMounted()) {
            this.state.data.push(account);
            this.setState({data: this.state.data});
        }
    },
    componentDidMount: function() {
        this.loadAccountList();
        subscribe('UserAccountCreatedEventWebsocketMessage', this.appendAccount);
        subscribe('ProductAccountCreatedEventWebsocketMessage', this.appendAccount);
    },
    render: function() {
        var accountNodes = this.state.data.map(function (account) {
            return (
                <li key={account.id} className="account">
                    <Account account={account} />
                </li>
            );
        });
        return (
            <div className="accountList" ref="accountList">
                <ul id="accounts">
                    {accountNodes}
                </ul>
            </div>
        );
    }
});
var UserPage = React.createClass({
    render: function() {
        return (
            <div className="userPage">
                <UserAccountForm />
                <AccountList url="/domain/accounts/user" type="user" />
            </div>
        );
    }
});
var ProductPage = React.createClass({
    render: function() {
        return (
            <div className="productPage">
                <ProductAccountForm />
                <UserWithBalance account={currentAccount} />
                <ToUserPageButton />
                <AccountList url="/domain/accounts/product" type="product" />
            </div>
        );
    }
});
ReactDOM.render(<UserPage />, document.getElementById('content'));