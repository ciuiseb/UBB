using System.Windows.Forms;
using log4net;
using Microsoft.VisualBasic.Logging;

namespace Triathlon.Client.WinForms.Utils
{
    public static class MessageUtil
    {
        private static readonly ILog logger = LogManager.GetLogger(typeof(MessageUtil));

        public static void ShowAlert(string title, string message)
        {
            MessageBox.Show(message, title, MessageBoxButtons.OK, MessageBoxIcon.Information);
        }

        public static void ShowError(string message)
        {
            MessageBox.Show(message, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }
    }
}