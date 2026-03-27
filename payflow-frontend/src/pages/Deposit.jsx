import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { userService } from '../services/userService';
import Layout from '../components/Layout';
import { PlusCircle, AlertCircle, CheckCircle, ArrowLeft } from 'lucide-react';

const Deposit = () => {
  const { user, updateUser } = useAuth();
  const [amount, setAmount] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const quickAmounts = [50, 100, 200, 500];

  const handleSubmit = async (e) => {
    e.preventDefault();
    await processDeposit(parseFloat(amount));
  };

  const handleQuickDeposit = async (value) => {
    setAmount(value.toString());
    await processDeposit(value);
  };

  const processDeposit = async (numAmount) => {
    setError('');
    setSuccess(false);

    if (isNaN(numAmount) || numAmount <= 0) {
      setError('Digite um valor válido');
      return;
    }

    setLoading(true);

    try {
      const updatedUser = await userService.deposit(numAmount);
      updateUser(updatedUser);
      setSuccess(true);
      setAmount('');
      setTimeout(() => {
        navigate('/dashboard');
      }, 2000);
    } catch (err) {
      setError(err.response?.data?.message || 'Erro ao realizar depósito');
    } finally {
      setLoading(false);
    }
  };

  const formatCurrency = (value) => {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL',
    }).format(value);
  };

  return (
    <Layout>
      <div className="max-w-2xl mx-auto">
        <button
          onClick={() => navigate('/dashboard')}
          className="flex items-center space-x-2 text-gray-600 hover:text-gray-900 mb-6 transition duration-150"
        >
          <ArrowLeft className="h-5 w-5" />
          <span>Voltar ao Dashboard</span>
        </button>

        <div className="bg-white rounded-xl shadow-lg p-8">
          <div className="flex items-center space-x-3 mb-6">
            <div className="bg-green-600 p-3 rounded-full">
              <PlusCircle className="h-6 w-6 text-white" />
            </div>
            <h2 className="text-2xl font-bold text-gray-900">Depositar</h2>
          </div>

          <div className="mb-6 p-4 bg-blue-50 border border-blue-200 rounded-lg">
            <p className="text-sm text-blue-800">
              <span className="font-medium">Saldo atual:</span> {formatCurrency(user?.balance || 0)}
            </p>
          </div>

          {error && (
            <div className="mb-4 bg-red-50 border border-red-200 text-red-600 px-4 py-3 rounded-lg flex items-center space-x-2">
              <AlertCircle className="h-5 w-5" />
              <span className="text-sm">{error}</span>
            </div>
          )}

          {success && (
            <div className="mb-4 bg-green-50 border border-green-200 text-green-600 px-4 py-3 rounded-lg flex items-center space-x-2">
              <CheckCircle className="h-5 w-5" />
              <span className="text-sm">Depósito realizado com sucesso!</span>
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-6">
            <div>
              <label htmlFor="amount" className="block text-sm font-medium text-gray-700 mb-2">
                Valor do Depósito
              </label>
              <div className="relative">
                <span className="absolute left-4 top-1/2 transform -translate-y-1/2 text-gray-500 text-lg">R$</span>
                <input
                  id="amount"
                  type="number"
                  step="0.01"
                  min="0.01"
                  required
                  value={amount}
                  onChange={(e) => setAmount(e.target.value)}
                  className="w-full pl-12 pr-4 py-3 text-lg border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent"
                  placeholder="0,00"
                />
              </div>
            </div>

            <div>
              <p className="text-sm font-medium text-gray-700 mb-3">Valores rápidos</p>
              <div className="grid grid-cols-4 gap-3">
                {quickAmounts.map((value) => (
                  <button
                    key={value}
                    type="button"
                    onClick={() => handleQuickDeposit(value)}
                    disabled={loading}
                    className="px-4 py-3 bg-gray-100 hover:bg-gray-200 text-gray-700 font-medium rounded-lg transition duration-150 disabled:opacity-50 disabled:cursor-not-allowed"
                  >
                    {formatCurrency(value)}
                  </button>
                ))}
              </div>
            </div>

            <div className="flex space-x-4">
              <button
                type="button"
                onClick={() => navigate('/dashboard')}
                className="flex-1 px-6 py-3 border border-gray-300 rounded-lg text-gray-700 font-medium hover:bg-gray-50 transition duration-150"
              >
                Cancelar
              </button>
              <button
                type="submit"
                disabled={loading}
                className="flex-1 flex items-center justify-center space-x-2 px-6 py-3 bg-green-600 text-white font-medium rounded-lg hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 disabled:opacity-50 disabled:cursor-not-allowed transition duration-150"
              >
                {loading ? (
                  <>
                    <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-white"></div>
                    <span>Processando...</span>
                  </>
                ) : (
                  <>
                    <PlusCircle className="h-5 w-5" />
                    <span>Depositar</span>
                  </>
                )}
              </button>
            </div>
          </form>

          <div className="mt-6 p-4 bg-green-50 rounded-lg">
            <p className="text-xs text-green-800">
              <span className="font-medium">Nota:</span> Este é um depósito simulado para fins de demonstração. 
              Em um sistema real, seria necessária integração com gateway de pagamento.
            </p>
          </div>
        </div>
      </div>
    </Layout>
  );
};

export default Deposit;
