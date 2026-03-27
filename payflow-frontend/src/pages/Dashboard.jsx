import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { transactionService } from '../services/transactionService';
import Layout from '../components/Layout';
import { ArrowUpRight, ArrowDownRight, Send, PlusCircle, RefreshCw, Copy, Check } from 'lucide-react';

const Dashboard = () => {
  const { user, updateUser } = useAuth();
  const [transactions, setTransactions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [copied, setCopied] = useState(false);
  const navigate = useNavigate();

  const loadData = async () => {
    setLoading(true);
    try {
      const history = await transactionService.getHistory();
      setTransactions(history);
    } catch (error) {
      console.error('Erro ao carregar dados:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const formatCurrency = (value) => {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL',
    }).format(value);
  };

  const formatDate = (dateString) => {
    return new Intl.DateTimeFormat('pt-BR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    }).format(new Date(dateString));
  };

  const isReceived = (transaction) => {
    return transaction.receiverName === user?.name;
  };

  const handleCopyId = async () => {
    try {
      await navigator.clipboard.writeText(user?.id || '');
      setCopied(true);
      setTimeout(() => setCopied(false), 2000);
    } catch (err) {
      console.error('Erro ao copiar ID:', err);
    }
  };

  return (
    <Layout>
      <div className="space-y-6">
        {/* Header */}
        <div className="flex justify-between items-center">
          <h1 className="text-3xl font-bold text-gray-900">Dashboard</h1>
          <button
            onClick={loadData}
            className="flex items-center space-x-2 px-4 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-lg hover:bg-gray-50 transition duration-150"
          >
            <RefreshCw className="h-4 w-4" />
            <span>Atualizar</span>
          </button>
        </div>

        {/* Balance Card */}
        <div className="bg-gradient-to-br from-primary-600 to-purple-600 rounded-xl shadow-lg p-8 text-white">
          <p className="text-sm opacity-90 mb-2">Saldo disponível</p>
          <h2 className="text-4xl font-bold mb-6">{formatCurrency(user?.balance || 0)}</h2>
          <div className="flex space-x-4">
            <button
              onClick={() => navigate('/transfer')}
              className="flex-1 flex items-center justify-center space-x-2 bg-white/20 hover:bg-white/30 backdrop-blur-sm px-6 py-3 rounded-lg transition duration-150"
            >
              <Send className="h-5 w-5" />
              <span className="font-medium">Transferir</span>
            </button>
            <button
              onClick={() => navigate('/deposit')}
              className="flex-1 flex items-center justify-center space-x-2 bg-white/20 hover:bg-white/30 backdrop-blur-sm px-6 py-3 rounded-lg transition duration-150"
            >
              <PlusCircle className="h-5 w-5" />
              <span className="font-medium">Depositar</span>
            </button>
          </div>
        </div>

        {/* User ID Card */}
        <div className="bg-white rounded-xl shadow-md p-6">
          <h3 className="text-lg font-semibold text-gray-900 mb-3">Meu ID para Receber Transferências</h3>
          <div className="flex items-center space-x-2">
            <code className="flex-1 px-4 py-3 bg-gray-100 rounded-lg text-sm font-mono text-gray-800 overflow-x-auto">
              {user?.id}
            </code>
            <button
              onClick={handleCopyId}
              className={`flex items-center space-x-2 px-4 py-3 rounded-lg font-medium transition duration-150 ${
                copied
                  ? 'bg-green-600 text-white'
                  : 'bg-primary-600 text-white hover:bg-primary-700'
              }`}
            >
              {copied ? (
                <>
                  <Check className="h-4 w-4" />
                  <span>Copiado!</span>
                </>
              ) : (
                <>
                  <Copy className="h-4 w-4" />
                  <span>Copiar</span>
                </>
              )}
            </button>
          </div>
          <p className="mt-2 text-xs text-gray-500">
            Compartilhe este ID com quem deseja te enviar dinheiro
          </p>
        </div>

        {/* Transaction History */}
        <div className="bg-white rounded-xl shadow-md overflow-hidden">
          <div className="px-6 py-4 border-b border-gray-200">
            <h3 className="text-lg font-semibold text-gray-900">Histórico de Transações</h3>
          </div>

          {loading ? (
            <div className="p-8 flex justify-center">
              <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600"></div>
            </div>
          ) : transactions.length === 0 ? (
            <div className="p-8 text-center text-gray-500">
              <p>Nenhuma transação encontrada</p>
            </div>
          ) : (
            <div className="divide-y divide-gray-200">
              {transactions.map((transaction) => {
                const received = isReceived(transaction);
                return (
                  <div key={transaction.id} className="px-6 py-4 hover:bg-gray-50 transition duration-150">
                    <div className="flex items-center justify-between">
                      <div className="flex items-center space-x-4">
                        <div className={`p-2 rounded-full ${received ? 'bg-green-100' : 'bg-red-100'}`}>
                          {received ? (
                            <ArrowDownRight className={`h-5 w-5 text-green-600`} />
                          ) : (
                            <ArrowUpRight className={`h-5 w-5 text-red-600`} />
                          )}
                        </div>
                        <div>
                          <p className="font-medium text-gray-900">
                            {received ? `De ${transaction.senderName}` : `Para ${transaction.receiverName}`}
                          </p>
                          <p className="text-sm text-gray-500">{formatDate(transaction.createdAt)}</p>
                        </div>
                      </div>
                      <div className={`text-lg font-semibold ${received ? 'text-green-600' : 'text-red-600'}`}>
                        {received ? '+' : '-'} {formatCurrency(transaction.amount)}
                      </div>
                    </div>
                  </div>
                );
              })}
            </div>
          )}
        </div>
      </div>
    </Layout>
  );
};

export default Dashboard;
