import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faWallet, faPiggyBank, faLandmark } from '@fortawesome/free-solid-svg-icons';
import '../styles/accounts.css'; // Make sure this includes .cs-button-solid or import the specific CSS file
import { getAccounts } from '../utils/accUtils';

function Accounts() {
  const [accountList, setAccountList] = useState({
    checking: {
      icon: <FontAwesomeIcon icon={faWallet} />,
      balance: 1500,
      onClick: () => navigate('/checking'),
    },
    savings: {
      icon: <FontAwesomeIcon icon={faPiggyBank} />,
      balance: 3000,
      onClick: () => navigate('/saving'),
    },
    market: {
      icon: <FontAwesomeIcon icon={faLandmark} />,
      balance: 5000,
      onClick: () => navigate('/moneymarket'),
    },
  });

  useEffect(() => {
    getAccounts().then(res => {
      if (res) {
        setAccountList({
          checking: {
            icon: <FontAwesomeIcon icon={faWallet} />,
            balance: res[0].balance,
            onClick: () => navigate('/checking'),
          },
          savings: {
            icon: <FontAwesomeIcon icon={faPiggyBank} />,
            balance: res[1].balance,
            onClick: () => navigate('/saving'),
          },
          market: {
            icon: <FontAwesomeIcon icon={faLandmark} />,
            balance: res[2].balance,
            onClick: () => navigate('/moneymarket'),
          },
        });
      }
    });
  }, []);

  const navigate = useNavigate();

  // const accounts = {
  //   checking: {
  //     icon: <FontAwesomeIcon icon={faWallet} />,
  //     balance: 1500,
  //     onClick: () => navigate('/checking'),
  //   },
  //   savings: {
  //     icon: <FontAwesomeIcon icon={faPiggyBank} />,
  //     balance: 3000,
  //     onClick: () => navigate('/saving'),
  //   },
  //   market: {
  //     icon: <FontAwesomeIcon icon={faLandmark} />,
  //     balance: 5000,
  //     onClick: () => navigate('/moneymarket'),
  //   },
  // };

  return (
    <div className="outer-div">
      <div className="content-div">
        <p className="welcome">Welcome to your account dashboard!</p>
        <p>
          Here, you can manage all your banking needs in one convenient place. Simply select the account you wish to view, and click "View
          Transactions" to see all recent activity and manage your finances effectively.
        </p>
      </div>
      <div className="accounts-container">
        {Object.entries(accountList).map(([key, { icon, balance, onClick }]) => (
          <div key={key} className="account-card">
            {icon}
            <h2>{key.charAt(0).toUpperCase() + key.slice(1)} Account</h2>
            <p>Balance: ${balance}</p>
            <button className="cs-button-solid" onClick={onClick}>
              View Transactions
            </button>
          </div>
        ))}
      </div>
    </div>
  );
}

export default Accounts;
